# -*-: coding: utf-8 -*-

#%%
import os

import pandas as pd

rootPath = os.getcwd()
projPath = os.path.join(rootPath, 'capitalflow')

dfp = pd.read_csv(os.path.join(projPath, 'data/user_profile_table.csv'))
dfb = pd.read_csv(os.path.join(projPath, 'data/user_balance_table.csv'))
dfbs = pd.read_csv(os.path.join(projPath, 'data/mfd_bank_shibor.csv'))
dfdsi = pd.read_csv(os.path.join(projPath, 'data/mfd_day_share_interest.csv'))

#%%
dfp.info()

total = dfp.shape[0]
man = dfp[dfp['sex'] == 1].shape[0]
women = dfp[dfp['sex'] == 0].shape[0]
print("样本总人数%s；\n男性人数%s，占比%s；\n女性人数%s，占比%s" % (total, man, man/total, women, women/total))
#%%
print(dfp['city'].unique())
print(dfp['city'].groupby().count())

#%%
