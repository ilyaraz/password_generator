import sys

symb = []
symb += [chr(x + ord('A')) for x in xrange(26)]
symb += [chr(x + ord('a')) for x in xrange(26)]
symb += [chr(x + ord('0')) for x in xrange(10)]
symb += [x for x in "!@#$%^&*()-_=+`~[{]};:'\"\\|,<.>/?"]

def random_byte():
    input = open("/dev/urandom", "rb")
    return ord(input.read(1))

if len(sys.argv) != 3:
    raise Exception("usage: python gen.py <password-length> <#passwords>")
 
length = int(sys.argv[1])
amount = int(sys.argv[2])

for it in xrange(amount):
    for i in xrange(length):
        x = len(symb)
        while x >= len(symb): 
            x = random_byte()
        sys.stdout.write(symb[x])
    sys.stdout.write("\n")
