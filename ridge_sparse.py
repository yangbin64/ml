import numpy as np
import ridge as r
import time

n_samples = 5000000
n_features = 400

n_blocks = 4
n_units = 100

print('starting...')
S = np.random.randint(n_units, size=(n_samples, n_blocks))
print('setting...')
X = np.array([[0 for i in range(n_features)] for j in range(n_samples)])
print('setting2...')
for i in range(n_samples):
    if i%50000==0:
        print(i)
    for j in range(n_blocks):
        X[i][j * n_units + S[i][j]] = 1
print(X)

b = np.random.rand(n_features, 1)*2.
print(b)

y = np.dot(X, b) + 6.
print(y)

yX = np.hstack((y.reshape(n_samples,1), X))
np.savetxt("yX.txt", yX, delimiter=',')

start = time.time()
print(start)
ridge = r.Ridge(alpha=0.000001)
coef = ridge.fit('yX.txt', n_samples, n_features)
print(coef)
end = time.time()
print(end)

