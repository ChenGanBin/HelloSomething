# -*-: coding: utf-8 -*-

#%%
import os
import pickle
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

from sklearn.model_selection import KFold, train_test_split, StratifiedKFold, cross_val_score, GridSearchCV
from sklearn.pipeline import Pipeline
from sklearn.linear_model import SGDClassifier, LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import log_loss, roc_auc_score, auc, roc_curve
from sklearn.preprocessing import MinMaxScaler

import xgboost as xgb
import lightgbm as lgb

from datetime import date

rootPath = os.getcwd()
projPath = os.path.join(rootPath, 'o2o')

# 线下训练集
dfoff = pd.read_csv(os.path.join(projPath, 'data/ccf_offline_stage1_train.csv'), keep_default_na=False)
# 线上训练集
dfon = pd.read_csv(os.path.join(projPath, 'data/ccf_online_stage1_train.csv'), keep_default_na=False)
# 测试集
dftest = pd.read_csv(os.path.join(projPath, 'data/ccf_offline_stage1_test_revised.csv'), keep_default_na=False)

#%%
print('有优惠券，购买商品条数', dfoff[(dfoff['Date_received'] != 'null') & (dfoff['Date'] != 'null')].shape[0])
print('无优惠券，购买商品条数', dfoff[(dfoff['Date_received'] == 'null') & (dfoff['Date'] != 'null')].shape[0])
print('有优惠券，不购买商品条数', dfoff[(dfoff['Date_received'] != 'null') & (dfoff['Date'] == 'null')].shape[0])
print('无优惠券，不购买商品条数', dfoff[(dfoff['Date_received'] == 'null') & (dfoff['Date'] == 'null')].shape[0])

# 在测试集中出现的用户但训练集没有出现
print('1. User_id in training set but not in test set', set(dftest['User_id']) - set(dfoff['User_id']))
# 在测试集中出现的商户但训练集没有出现
print('2. Merchant_id in training set but not in test set', set(dftest['Merchant_id']) - set(dfoff['Merchant_id']))

#%%
# 优惠券和距离
print('Discount_rate 类型:',dfoff['Discount_rate'].unique())
print('Distance 类型:', dfoff['Distance'].unique())

#处理方式：将str变成 numeric
#1.将满xx减yy类型(xx:yy)的券变成折扣率 : 1 - yy/xx，同时建立折扣券相关的特征 discount_rate, discount_man, discount_jian, discount_type
#2.将距离 str 转为 int
def getDiscountType(row):
    if row == 'null':
        return 'null'
    elif ':' in row:
        return 1
    else:
        return 0

def convertRate(row):
    """Convert discount to rate"""
    if row == 'null':
        return 1.0
    elif ':' in row:
        rows = row.split(':')
        return 1.0 - float(rows[1])/float(rows[0])
    else:
        return float(row)

def getDiscountMan(row):
    if ':' in row:
        rows = row.split(':')
        return int(rows[0])
    else:
        return 0

def getDiscountJian(row):
    if ':' in row:
        rows = row.split(':')
        return int(rows[1])
    else:
        return 0

def processData(df):

    # convert discunt_rate
    df['discount_rate'] = df['Discount_rate'].apply(convertRate)
    df['discount_man'] = df['Discount_rate'].apply(getDiscountMan)
    df['discount_jian'] = df['Discount_rate'].apply(getDiscountJian)
    df['discount_type'] = df['Discount_rate'].apply(getDiscountType)
    print(df['discount_rate'].unique())

    # convert distance
    df['distance'] = df['Distance'].replace('null', -1).astype(int)
    print(df['distance'].unique())
    return df

dfoff = processData(dfoff)
dftest = processData(dftest)

dfoff.head(5)


#%%
#时间
date_received = dfoff['Date_received'].unique()
date_received = sorted(date_received[date_received != 'null'])

date_buy = dfoff['Date'].unique()
date_buy = sorted(date_buy[date_buy != 'null'])
# 效果如上 date_buy = sorted(dfoff[dfoff['Date'] != 'null']['Date'])

print('优惠券收到日期从',date_received[0],'到', date_received[-1])
print('消费日期从', date_buy[0], '到', date_buy[-1])


#%%
#收到coupon的数目，以及收到coupon后用coupon消费的数目
couponbydate = dfoff[dfoff['Date_received'] != 'null'][['Date_received', 'Date']].groupby(['Date_received'], as_index=False).count()
couponbydate.columns = ['Date_received','count']
buybydate = dfoff[(dfoff['Date'] != 'null') & (dfoff['Date_received'] != 'null')][['Date_received', 'Date']].groupby(['Date_received'], as_index=False).count()
buybydate.columns = ['Date_received','count']

sns.set_style('ticks')
sns.set_context("notebook", font_scale= 1.4)
plt.figure(figsize = (12,8))
date_received_dt = pd.to_datetime(date_received, format='%Y%m%d')

plt.subplot(211)
plt.bar(date_received_dt, couponbydate['count'], label = 'number of coupon received' )
plt.bar(date_received_dt, buybydate['count'], label = 'number of coupon used')
plt.yscale('log')
plt.ylabel('Count')
plt.legend()

plt.subplot(212)
plt.bar(date_received_dt, buybydate['count']/couponbydate['count'])
plt.ylabel('Ratio(coupon used/coupon received)')
plt.tight_layout()

#%%
#新建关于星期的特征
#weekday: {1,2,3,4,5,6,7}
#weekday_1, ..., weekday_7: {0,1}
def getWeekday(row):
    if row == 'null':
        return row
    else:
        return date(int(row[0:4]), int(row[4:6]), int(row[6:8])).weekday() + 1

dfoff['weekday'] = dfoff['Date_received'].astype(str).apply(getWeekday)
dftest['weekday'] = dftest['Date_received'].astype(str).apply(getWeekday)

# weekday_type :  周六和周日为1，其他为0
dfoff['weekday_type'] = dfoff['weekday'].apply(lambda x : 1 if x in [6,7] else 0 )
dftest['weekday_type'] = dftest['weekday'].apply(lambda x : 1 if x in [6,7] else 0 )

# change weekday to one-hot encoding
weekdaycols = ['weekday_' + str(i) for i in range(1,8)]

tmpdf = pd.get_dummies(dfoff['weekday'].replace('null', np.nan))
tmpdf.columns = weekdaycols
dfoff[weekdaycols] = tmpdf

tmpdf = pd.get_dummies(dftest['weekday'].replace('null', np.nan))
tmpdf.columns = weekdaycols
dftest[weekdaycols] = tmpdf


#%%
#数据标注-三类：
#Date_received == 'null': y = -1
#Date != 'null' & Date-Date_received <= 15: y = 1
#Otherwise: y = 0
def label(row):
    if row['Date_received'] == 'null':
        return -1
    if row['Date'] != 'null':
        td = pd.to_datetime(row['Date'], format='%Y%m%d') -  pd.to_datetime(row['Date_received'], format='%Y%m%d')
        if td <= pd.Timedelta(15, 'D'):
            return 1
    return 0
dfoff['label'] = dfoff.apply(label, axis = 1)
print(dfoff['label'].value_counts())
print('已有columns：',dfoff.columns.tolist())
dfoff.head(2)

#%%
#最初模型
#直接用 discount, distance, weekday 类的特征。
#train/valid 的划分：用20160101到20160515的作为train，20160516到20160615作为valid。
#用线性模型 SGDClassifier

# 数据分割 split
df = dfoff[dfoff['label'] != -1].copy()
train = df[(df['Date_received'] < '20160516')].copy()
valid = df[(df['Date_received'] >= '20160516') & (df['Date_received'] <= '20160615')].copy()
print(train['label'].value_counts())
print(valid['label'].value_counts())

# 特征
original_feature = ['discount_rate','discount_type','discount_man', 'discount_jian','distance', 'weekday', 'weekday_type'] + weekdaycols
print(len(original_feature), original_feature)

#模型
predictors = original_feature
print(predictors)

def check_model(data, predictors):

# SGD随机梯度
    classifier = lambda: SGDClassifier(
        loss='log',
        penalty='elasticnet',
        fit_intercept=True,
        max_iter=100,
        shuffle=True,
        n_jobs=1,
        class_weight=None)

    model = Pipeline(steps=[
        ('ss', StandardScaler()),
        ('en', classifier())
    ])

    parameters = {
        'en__alpha': [ 0.001, 0.01, 0.1],
        'en__l1_ratio': [ 0.001, 0.01, 0.1]
    }

# k折交叉切分
    folder = StratifiedKFold(n_splits=3, shuffle=True)

# CV自动调参
# 超参数自动搜索模块
    grid_search = GridSearchCV(
        model,
        parameters,
        cv=folder,
        n_jobs=-1,
        verbose=1)
    grid_search = grid_search.fit(data[predictors],
                                  data['label'])

    return grid_search

if not os.path.isfile('1_model.pkl'):
    model = check_model(train, predictors)
    print(model.best_score_)
    print(model.best_params_)
    with open('1_model.pkl', 'wb') as f:
        pickle.dump(model, f)
else:
    with open('1_model.pkl', 'rb') as f:
        model = pickle.load(f)

# valid predict
y_valid_pred = model.predict_proba(valid[predictors])
valid1 = valid.copy()
valid1['pred_prob'] = y_valid_pred[:, 1]
valid1.head(2)

# avgAUC calculation
vg = valid1.groupby(['Coupon_id'])
aucs = []
for i in vg:
    tmpdf = i[1]
    if len(tmpdf['label'].unique()) != 2:
        continue
    fpr, tpr, thresholds = roc_curve(tmpdf['label'], tmpdf['pred_prob'], pos_label=1)
    aucs.append(auc(fpr, tpr))
print(np.average(aucs))

# test prediction for submission
y_test_pred = model.predict_proba(dftest[predictors])
dftest1 = dftest[['User_id','Coupon_id','Date_received']].copy()
dftest1['label'] = y_test_pred[:,1]
dftest1.to_csv('submit1.csv', index=False, header=False)
dftest1.head()

#%%
