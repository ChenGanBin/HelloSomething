# -*-: coding: utf-8 -*-

#%%
import os
import pandas as pd

rootPath = os.getcwd()
projPath = os.path.join(rootPath, 'o2o')

# 线下训练集
dfoff = pd.read_csv(os.path.join(projPath, 'data/ccf_offline_stage1_train.csv'))
# 线上训练集
dfon = pd.read_csv(os.path.join(projPath, 'data/ccf_online_stage1_train.csv'))
# 测试集
dftest = pd.read_csv(os.path.join(projPath, 'data/ccf_offline_stage1_test_revised.csv'))


#%%
dfoff.info()
