import sys

symb = [chr(x) for x in xrange(33, 127)]

def random_byte():
    input = open("/dev/random", "rb")
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
