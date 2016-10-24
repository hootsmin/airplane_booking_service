Airplane Booking Service
========================

This is the classical bin packing problem albeit with a fixed number of bins (i.e. rows). Finding an optimal solution is NP hard, meaning we have to go through every possible combination o determine the best possible solution.

 The rules governing customer satisfaction in this solution are as follow:
 
 	- A customer is only considered satisfied if they are seated together in their group AND their seat preference is satisfied (if one exists).

Two potential solutions are presented.

1. Optimal brute force
----------------------

First of all, we calculate a rough estimate for the maximum number of customers who can potentially be satisfied given the number of seats on the plane and the size of each group.

If there are any groups larger than the number of seats in a row reduce the maximum potential total of satisfied customers by the size of this group.

If, after performing the above step there are still more customers than seats then reduce maximum potential satisfied customers to the number of seats on the plane.

Next, determine all possible combinations of groups. Loop over them, assigning the groups to the plane in the order given and then calculate the percentage of satisfied customers. If this figure is greater or equal to our estimated optimal then stop processing the list of groups.

2. Sort decreasing then by window seat preferences
--------------------------------------------------

This does not always produce the most optimal solution but is exponentially quicker than the brute force approach.

First of all, sort the groups by size (decreasing). Where the groups are the same size the following logic is used:

	- If the group size is equal to the row size then sort those with 2 window seat preference before other groups.
	- Next, pick groups with one window preference.
	- Next, pick groups with no window preference over the rest.
	
All solutions presented use the same logic to assign a group to the plane (i.e. place in the first row the group fits). If there is a window seat available it will be assigned to the person in the group with a window seat preference (thus ensuring maximum customer satisfaction).

Running Program
---------------

It is possible to run the program with either solution. The program will default to the sort decreasing order then by window seat preference option.

java -cp <jar file> com.matt.airplane.booking.service.AirplaneBookingServiceApp <input file> [-b]

Specify -b for optimal brute force.

Next Steps
----------

The following improvements could be made:
	
1. Look at faster alternatives to optimal brute force approach, such as that presented by Korf (http://aaaipress.org/Papers/AAAI/2002/AAAI02-110.pdf).

2. Improve mechanism to estimate optimal customer satisfaction. 

	- Order groups by size (decreasing).
	- Remove those groups that are too big to fit in a row.
	- If total customers in remaining groups is larger than seats in plane remove group
	- Repeat until total customers in remaining groups is less than or equal to number of seats in plane.
	- Calculate number of window seat preferences in remaining groups. If this is greater than number of window seats in plane then reduce number of potential satisfied customers appropriately.
	
3. Update mechanism for assigning groups that are too large to fit in a row but that can still all be accommodated in plane. This would involve attempting to keep group members together. However, as this would still not increase satisfaction levels (as defined above) it was decided to forgo implementing.

4. For unit tests, where there is a dependency between objects (e.g. between plane and row), use mock objects rather than creating real instances.

5. Refactor code in the controller, splitting out some of the helper methods into separate helper classes to aid code readability.   
  