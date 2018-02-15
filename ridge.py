import numpy as np
from scipy import linalg
import time

class Ridge:
    #@abstractmethod
    def __init__(self, alpha=1.0):
        self.alpha = alpha

    def compute_A_Xy(self, filename, n_samples, n_features):
        A = np.array([[0.0 for i in range(n_features+1)] for j in range(n_features+1)])
        Xy = np.array([[0.0 for i in range(1)] for j in range(n_features+1)])

        f_yX = open(filename, 'r')
        line = f_yX.readline()
        cnt = 0
        while line:
            cnt = cnt + 1
            if cnt%100000 == 0:
                print(cnt)
            words = line.split('\n')[0].split(',')
            values = np.array([float(x) for x in words if x])
            #print(values)

            values_y = values[0]
            values_X = values[1:]
            #print(values_X)
            values_X = np.append(values_X, 1.)
            #print(values_X)
            A = A + np.dot(values_X.reshape(n_features+1,1), values_X.reshape(1,n_features+1))
            Xy = Xy + np.dot(values_X.reshape(n_features+1,1), values_y.reshape(1,1))

            line = f_yX.readline()
        f_yX.close

        return A, Xy

    def compute_A_Xy_sparse(self, filename, n_samples, n_features):
        A = np.array([[0.0 for i in range(n_features+1)] for j in range(n_features+1)])
        Xy = np.array([[0.0 for i in range(1)] for j in range(n_features+1)])

        CONST1 = 1.0#/n_samples
        #CONST2 = 1.0/np.sqrt(n_samples)

        f_yX = open(filename, 'r')
        line = f_yX.readline()
        cnt = 0
        while line:
            cnt = cnt + 1
            if cnt%100000 == 0:
                print(cnt)
            words = line.split('\n')[0].split(',')
            words_X = words[1:]
            values_y = float(words[0])
            values_X = np.array([int(x) for x in words_X if x])
            values_X = np.append(values_X, n_features)
            #print(values_X)

            for i in range(len(values_X)):
                ii = values_X[i]
                Xy[ii] = Xy[ii] + CONST1*values_y

                for j in range(len(values_X)):
                    jj = values_X[j]
                    A[ii,jj] = A[ii,jj] + CONST1

            line = f_yX.readline()
        f_yX.close

        return A, Xy

    def fit(self, filename, n_samples, n_features):
        #A, Xy = self.compute_A_Xy(filename, n_samples, n_features)
        A, Xy = self.compute_A_Xy_sparse(filename, n_samples, n_features)

        mid = time.time()
        print(mid)

        print(A)
        for i in range(n_features+1):
            A[i][i] = A[i][i] + self.alpha
        #A.flat[::n_features + 1] += alpha[0]
        print('A:', A)
        print(linalg.solve(A, Xy, sym_pos=True,
                            overwrite_a=True).T)
        return linalg.solve(A, Xy, sym_pos=True,
                            overwrite_a=True).T

