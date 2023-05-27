- Arrays  
For collections of things that wouldn't change size while running  
Fastest access time O(1) (ArrayList can be a bit slower since there are layers to it, but it is still O(1))  
- ArrayList  
For the 'arrays' of MaliSkript, fast access time O(1) / changing the value inside it  
Can dynamically change  
- LinkedLists (this class in Java is actually doubly linked lists)  
For the results (like print/println) since they can be added and removed pretty quickly  
- HashMap  
Many things in my project use it, including the variables, arrays, etc.  
The keys are in general Strings since they are used to store the variable/array name  
Fast insertion/deletion O(1)  
Fast access O(1)  
- Stack  
For the equation solver
Since the values are being pushed and popped in a FILO (First in Last out) way  
- Deque  
For the compilation errors  
Can be used as both a stack (FILO/LIFO) and a queue (FIFO/LILO)  
The user chooses how he wants to see the errors, from first to last or last to first  
  
