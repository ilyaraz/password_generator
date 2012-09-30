import hashlib
import sys
import getpass

symb = []
symb += [chr(x + ord('A')) for x in xrange(26)]
symb += [chr(x + ord('a')) for x in xrange(26)]
symb += [chr(x + ord('0')) for x in xrange(10)]
symb += [x for x in "!@#$%^&*()-_=+`~[{]};:'\"\\|,<.>/?"]

password = getpass.getpass()

h = hashlib.new('sha512')
h.update(password)
h.update(sys.argv[1])
h.update('')
a = list(h.digest())
num = 0
for c in a:
    num = num * 256 + ord(c)
for i in xrange(10):
    sys.stdout.write('%c' % symb[num % 94])
    num = num / 94
print
