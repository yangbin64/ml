import numpy as np
import ridge as r
import time

n_samples = 50000
n_features = 20

n_blocks = 4
n_units = [3,6,5,6]
n_bases = [0,3,9,14]
'''
b = [[],[],[],[]]
fb = open('b_s.txt', 'w')

for j in range(n_blocks):
    for k in range(n_units[j]):
        b[j].append(np.random.rand()*2.)
        fb.write(str(b[j][k]))
        fb.write(',')
    fb.write('\n')
b.append(6.)
fb.write(str(b[n_blocks]))

fb.close()
print(b)

f = open('yX_s.txt', 'w')
for i in range(n_samples):
    y = b[n_blocks]
    x = []
    for j in range(n_blocks):
        k = np.random.randint(n_units[j])
        x.append(k + n_bases[j])
        v = b[j][k]
        y = y + v
    f.write(str(y))
    for j in range(n_blocks):
        f.write(',')
        f.write(str(x[j]))
    f.write('\n')

f.close()

'''
start = time.time()
print(start)
ridge = r.Ridge(alpha=0.000001)
coef = ridge.fit('yX_s.txt', n_samples, n_features)
print(coef)
end = time.time()
print(end)


