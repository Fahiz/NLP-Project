#Create vocabulary from preprocessed reviews database
#input: "processed_data.txt" 
#output: "vocabulary.txt"
#First line of output file is vocabulary size

fo1 = open("list","wb+")
with open("processed_data.txt") as fo:
    for line in fo:
     for word in line.split():
        fo1.write(word)
	fo1.write('\n')
fo1.close()

import os
os.system("sort list -o list")

fo = open("op","wb+")
with open("list","r") as f1: 
    data=iter(f1.read().split())
    a= next(data)
    b= a
    l=1
    c=0;
    pl = "+"
    mi = "-"
    count = 0;
while True:
    try:
        a= b
        b= next(data)
        #print a,b
	if a == b:
	 c=c+1
	 if l == 1:
	  print a
	  fo.write(a)
	  fo.write('\n')
	  l=0;
	else:
	 c=c+1
	 if c > 1:
	  count = count + 1;
	  #fo.write('%d'%c)
	  #fo.write('\n')
	 c=0
	 l=1
    except StopIteration:
        print "No more pair"
	#fo.write('%d'%count)
        break
fo.close()
f1.close()

nom = 0

fo2 = open("op","r+")
fo3 = open("vocabulary.txt","wb+")
count = count - 1
fo3.write('%d'%count)
count = count + 2
fo3.write('\n')
line = fo2.readline()
line = fo2.readline()
for num in range(1,count):
 line = fo2.readline() 
 fo3.write('%s'%line)

fo2.close()
fo3.close()

import os, sys
os.remove("op")
os.remove("list")





