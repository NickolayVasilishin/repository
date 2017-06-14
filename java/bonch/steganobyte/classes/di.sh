xxd $1 > $1.hex
xxd $2 > $2.hex

kdiff3 $1.hex $2.hex

rm *.hex
