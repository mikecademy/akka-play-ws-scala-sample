Notes:

My StackOverflow answer about using actor for having parallel access to single file:

http://stackoverflow.com/questions/11576439/parallel-file-processing-in-scala/16574114#16574114

Some useful links (to read):
 http://www.ybrikman.com/writing/2013/11/24/play-scala-and-iteratees-vs-nodejs/
 https://www.playframework.com/documentation/2.0/Iteratees

Domain:

 History library
  - assosated to One file
  - contains: One Actor can write
  - contains: Many Actors can read (pool of actors)
  
 Client (demo)
   access view WS
    - will read entier file/data/history after connecting
    - will append data/messages to the file
    
    
  