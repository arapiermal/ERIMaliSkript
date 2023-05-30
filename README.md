# ERIMaliSkript
New pseudo programming languages in Java

### JavaFX powered GUI compiler
- HELP for syntax
### Save and Load .mskript, .eri files
(The syntax of the following programming languages can be easily changed, since they use an Enum for it)
# MaliSkript
Features
Are you tired of Java and its bureaucracy, and would you like some programming in Albanian?

Near zero punctuation marks , ; and parentheses {}[]() needed

Space button and Enter (New Line) is your friend

### Printo
- Command to show results
- printo vepro x^2 + 2*x + 1
- printo myVar is $myVar
- printo myArr at index $i is #myArr@i

### File
- Epic command to count how many times do the words of a text file repeat and show them (Using a HashMap for high efficiency)  
  Done through only one line of code: file numero book.txt
- file rendit book.txt -> each row from a to z OR file rendit mbrapsht book.txt -> each row from z to a
- file var shortName C:\Long FileName\So tiring\wow.txt
- above commands can also use the declared file variable
### Variables
- For now: Integers called using a String
- Main logic implemented through them
#### Equation solver (Variables can get into it)
- Can use different types of parentheses {[(Stack is used)]}
- Besides the Java +-*/%, it also has ^ for power and _ for root 😲 (example: 5 ^ 2 = 25; 3 _ 125 = 5)
- Can solve {3*[5-2*(2-1)]-2}^2+4 (The result is 53)


### Arrays
- Index starts from 1 (More intuitive for the layman)
- Access them like #myArray@index (no need for [][][][][][])

### functions, for and if don't need (){}(){}(){}

---> 

---> To signify their end simply write 'kaq'
#### Functions: easy
funksion test a b  
printo vepro a + b  
kaq  
thirr test 2 3  
~ 5 is the result  
#### For cycle: simplified  
- per 1 deri 10 ==> for(int i = 1; i <= 10; i ++)  
- per a deri b ==> for(int i = a; i <= b; i++) (nese a < b)  
- per 10 deri 1 ==> for(int i = 10; i >= 1; i--)  
#### If
- Simplified if else {if - nqs (Ne qofte se); else - pn (perndryshe)}
- nqs a < b    
  printo E vertete    
  pn    
  printo e gabuar    
  kaq
---------------------------------------------------
# MyERI
Ergonomical Resource Introspector
Database manager

- Create tables
- Create columns in the tables with a certain name and DataType
- Insert rows in the tables (the values have to match the DataType)
- Delete based on rows or columns
- Show data based on different things
  - Show all of the data of the table  
  - Column  

- Save & Load  
  - from .eri  
  - from CSV  

# MyEri and MaliSkript collab
- declaredVariable = db tableName columnName rowIndex ~ columnName has to be INT
