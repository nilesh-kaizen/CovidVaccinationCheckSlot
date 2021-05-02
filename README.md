# CovidVaccinationCheckSlot

Steps to run:

1.Make changes in checkSlot.bat file and modify below arguments as per user requirement.
  districtID - Provide districtId nearest to your location.
  
  How to find District ID:
  
    1. Go to given link - https://cdn-api.co-vin.in/api/v2/admin/location/states
  
    2. Search your district from above link
    
    3. Go to given link - https://cdn-api.co-vin.in/api/v2/admin/location/districts/21 21 is State ID for Maharashtra.
    
    4. Find district ID for your district and provide that ID in districtId argument of checkSlot.bat file.

2.Set ageLimit as per your requirement.

3.Set emailId to send email from.

4.Set password to password of emailId.

5.Set toEmailId to the email IDs you want to receive mail on availability of slots.
