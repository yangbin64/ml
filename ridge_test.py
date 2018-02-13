import numpy as np
import ridge as r
import time

n_samples = 5000000
n_features = 400

'''
X = np.random.rand(n_samples, n_features)*30.
print(X)
b = np.random.rand(n_features, 1)*10.
print(b)
y = np.dot(X, b) + 20.
print(y)
yX = np.hstack((y.reshape(n_samples,1), X))
np.savetxt("yX.txt", yX, delimiter=',')
'''

start = time.time()
print(start)
ridge = r.Ridge(alpha=0.000001)
coef = ridge.fit('yX.txt', n_samples, n_features)
print(coef)
end = time.time()
print(end)

