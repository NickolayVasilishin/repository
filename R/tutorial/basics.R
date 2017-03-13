#### help:
  ?help
  help("c")

#### install new package:
  install.packages("ggplot2")

#### vector:
  v = c("one","two","three","four","five")
  v[3]
  v[c(1,3,5)]
  v[2:4]

#### Sequence of numbers:
  2:10

#### matrices:
  help(matrix)
  matrix(1:20, nrow=4)

#### arrays (multi-dimensional matrices):
  help(array)
  d1 <- c('A1', 'A2')
  d2 <- c('B1', 'B2', 'B3')
  d3 <- c('C1', 'C2', 'C3', 'C4')
  z = array(1:24, dim = c(2,3,4), dimnames = list(d1, d2, d3))
  z
  z[1,2,3]

#### DataFrames (multi-type matrices):
  help(data.frame)
  patientID <- 1:3
  age <- c(21, 22, 23)
  diabetes <- c('Type1', 'Type2', 'Type3')
  status <- c('Poor', 'Improved', 'Excellent')
  patientDataFrame <- data.frame(patientID, age, diabetes, status)
  patientDataFrame
  patientDataFrame[1:2]
  patientDataFrame[c('age', 'status')]
  patientDataFrame$age
  
  # Doesn't work here because shades previously defined variable
  # But in fact used for short access to inner variables
  attach(patientDataFrame)
    print(age)
  detach(patientDataFrame)
    
  # The same thing, but works anyway
  with(patientDataFrame, {
    print(age)
  })
  
#### Factors - representation of nominal and ordinal features (e.g. diabetes type or status respectively)
  diabetes <- factor(c('Type1', 'Type2', 'Type3', 'Type2', 'Type3'))
  status <- factor(c('Poor', 'Improved', 'Excellent'), ordered = TRUE, levels = c('Poor', 'Improved', 'Excellent'))
  
#### Lists are ordered collection of elements, probably named
  list(1, 'c', var='string', c(1:10))
  
#### Import 
  # from stdin:
    data = edit(data)
    # equals to
    fix(data)
  # from file:
    read.table(file, ...)
  