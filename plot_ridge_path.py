# Author: Fabian Pedregosa -- <fabian.pedregosa@inria.fr>
# License: BSD 3 clause
print(__doc__)

import numpy as np
import matplotlib.pyplot as plt
from sklearn import linear_model
from Ridge import ridge

# X is the 10x10 Hilbert matrix
X = 1. / (np.arange(1, 11) + np.arange(0, 10)[:, np.newaxis])
y = np.ones(10)
yX = np.hstack((y.reshape(10,1), X))

#X = np.arange(15).reshape(5,3)+10
#b = np.array([2., 3., 4.]).reshape(3,1)+100.
#y = np.dot(X,b)
#yX = np.hstack((y.reshape(5,1), X))

#print(y)
#print(X)
#print(yX)
#np.savetxt("X.txt", X, delimiter=',')
#np.savetxt("y.txt", y, delimiter=',')
np.savetxt("yX.txt", yX, delimiter=',')

# #############################################################################
# Compute paths

#n_alphas = 200
n_alphas = 2
alphas = np.logspace(-10, -2, n_alphas)

print(alphas)
coefs = []
for a in alphas:
    print('>>>>>>>>>>')
    r0 = linear_model.Ridge(alpha=a, fit_intercept=False)
    #r0 = linear_model.Ridge(alpha=a)
    r0.fit(X, y)
    print(r0.coef_)
    print(r0.intercept_)
    
    r = ridge.Ridge(alpha=a, fit_intercept=False)
    #r = ridge.Ridge(alpha=a)
    r.fit_file(X, y)
    print(r.coef_)
    print(r.intercept_)
    
    coefs.append(r.coef_)
    print('<<<<<<<<<<')

# #############################################################################
# Display results

ax = plt.gca()

ax.plot(alphas, coefs)
ax.set_xscale('log')
ax.set_xlim(ax.get_xlim()[::-1])  # reverse axis
plt.xlabel('alpha')
plt.ylabel('weights')
plt.title('Ridge coefficients as a function of the regularization')
plt.axis('tight')
plt.show()
