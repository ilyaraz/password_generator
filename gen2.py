import hashlib
import sys
import getpass
import os

if len(sys.argv) != 3:
    raise Exception("usage: python gen1.py <clue> <password-length>")

clue = sys.argv[1]
length = int(sys.argv[2])

def shellquote(s):
    return "'" + s.replace("'", "'\\''") + "\c'"

symb = [chr(x + ord('a')) for x in xrange(26)]
symb += [chr(x + ord('A')) for x in xrange(26)]
symb += [chr(x + ord('0')) for x in xrange(10)]

password = getpass.getpass()

h = hashlib.new('sha512')
h.update(password)
h.update('')
print h.hexdigest()[:5]

h = hashlib.new('sha512')
h.update(password)
h.update(clue)
h.update('')
a = list(h.digest())
num = 0
for c in a:
    num = num * 256 + ord(c)
password = ""
for i in xrange(length):
    password += symb[num % len(symb)]
    num = num / len(symb)
os.system("echo %s | pbcopy" % shellquote(password))
raw_input("press any key")
os.system("echo blahblah | pbcopy")
