
##Day 3

*Strings*

Strings are immutable .

#Why can't we change Strings ?
1. Security
2. Thread Safety

String status = "ON TIME";
status = "DELAYED";

*String Constant Pool*
String name = "NAWAZ";

what is the difference between == and .equals()
String s1 = "Apple";
String s2 = "Apple";
String s3 = new String("Apple");

System.out.println(s1 == s2) ==> true
System.out.println(s1 == s3) ==> false. (s3 is seperate object in a Heap)
System.out.println(s1.equals(s3)) ==> true
