#!/usr/bin/python3
import sys

inp = open(sys.argv[1])
data = []
for x in inp:
	data += [x.strip().split()[0]]

data = data[:-1]
data = [int(x) for x in data]
count = dict()

for x in data:
	if not (x)//500 in count: count[(x)//500] = 1
	else :count[(x)//500] += 1

print(count)
