# DataEaze_assignment

Pre-requisites:
     To run this code,You should have spark installed on your machine.

Step 1:
     Download files abcd.sh and q1scala.scala.
     
Step 2:
     Upload both files to your EMR cluster by logging in using ssh key.
     
Step 3:
     bash abcd.sh
     
     
abcd.sh :
    This bash script file will point to q1scala.scala.So Once we trigger this bash file as "bash abcd.sh" ,execution will start by opening spark-shell.
    
q1scala.scala:
    This file has all code including taking input files and writing output to s3 directory and printing the same output on console.
    So here as input and output path ,I have used s3.
    Once execution gets complete,we will get separate directory for each output which will have output csv files.
    
solution_op_dataeaze:
    This file shows output which gets printed on console once we trigger bash abcd.sh
 
    
    
