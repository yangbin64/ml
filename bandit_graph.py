import numpy as np
import pandas as pd

df_all = None

for i in range(100):
    filename = '../model' + str(i) + '.csv'
    df = pd.read_csv(filename, header=-1, names=['class', 'arm', 'sum', 'n', 'sum2'])
    df['round'] = i
    df['mean'] = df['sum']/df['n']
    #print(df)
    if i==0:
        df_all=df
    else:
        df_all=pd.concat([df_all, df])

print(df_all)
df_all.to_csv('model_all.csv')
