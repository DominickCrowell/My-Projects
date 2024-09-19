# Programmers: Dominick Crowell (croweld@email.uscb.edu)
#              Alex Wyman (ahwyman@email.uscb.edu)
# Assignment:  B104 F21 Script G6

# 1. Age of Respondents Chart
# ---------------------------

import matplotlib.pyplot as plt
import numpy as np

with open("Standards.csv") as f:
    f.readline()  
    
    twelve = 0
    thirteen = 0
    fourteen = 0
    fifteen = 0
    sixteen = 0
    seventeen = 0
    eighteen = 0    
    
    for line in f:
        q1 = line.split(',')[0]
        if q1 == '1':
            twelve += 1
        elif q1 == '2':
            thirteen += 1
        elif q1 == '3':
            fourteen += 1
        elif q1 == '4':
            fifteen += 1
        elif q1 == '5':
            sixteen+= 1
        elif q1 =='6':
            seventeen += 1
        elif q1 == '7':
            eighteen += 1
            
print(line)
            
left = [1, 2, 3, 4, 5, 6, 7]
height = [twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen]
tick_label = [12, 13, 14, 15, 16, 17, 18]
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('Age of Students')
plt.ylabel('Respondents')
plt.title('Age of Respondents')
plt.show()

# 2. Male and Female Respondents
# ------------------------------

with open("Standards.csv") as f:
    f.readline()
    
    male = 0
    female = 0
    
    for line2 in f:
        second = line2.split(',')[1]
        if second == '1':
            male +=1
        elif second == '2': 
            female +=1  
            
left = [1, 2]
height = [male,female]
tick_label = [f'{male} Males',f'{female} Females']
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('Number of Males and Females')
plt.ylabel('')
plt.title('Males and Females')
plt.show()

# 3. Grade Level of Students
# --------------------------

with open("Standards.csv") as f:
    f.readline()
    
    ninth = 0
    tenth = 0
    eleventh = 0
    twelth = 0
    other = 0 
    
    for line3 in f:
        third = line3.split(',')[2]
        if third == '1':
            ninth +=1
        elif third == '2': 
            tenth +=1
        elif third == '3': 
            eleventh +=1
        elif third == '4': 
            twelth +=1
        elif third == '5': 
            other +=1  
            
left = [1,2,3,4,5]
height = [ninth,tenth,eleventh,twelth,other]
tick_label = [f'{ninth}',f'{tenth}',f'{eleventh}',f'{twelth}',f'{other}']
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('Grade Levels 9th - 12th and others')
plt.ylabel('Number of students in grade')
plt.title('In what grade are you? ')
plt.show()

# 4. Respondents by Ethnicity
# ---------------------------

with open("Standards.csv") as f:
    f.readline()
    
    aian = 0
    asian = 0
    black = 0
    nhpi = 0
    white = 0 
    
    for line in f:
        forty5 = line.split(',')[4]
        if forty5 == 'A':
            aian +=1
        elif forty5 == '12345':
            aian +=1
            asian +=1
            black +=1
            nhpi +=1
            white +=1            
        
        elif forty5 == '1':
            aian +=1
        
        elif forty5 =='12':
            aian +=1
            asian +=1
        
        elif forty5 =='123':
            aian +=1
            asian +=1
            black +=1
        
        elif forty5 =='1234':
            aian +=1
            asian +=1
            black +=1
            nhpi +=1
        
        elif forty5 == '1 3':
            aian +=1
            black +=1
        
        elif forty5 == '1 34':
            aian +=1
            black +=1
            nhpi +=1
        
        elif forty5 == '1 345':
            aian +=1
            black +=1
            nhpi +=1
            white +=1
        
        elif forty5 == '1  4':
            aian +=1
            nhpi +=1
        
        elif forty5 == '1   5':
            aian +=1
            white +=1            
        
        elif forty5 ==' 2':
            asian +=1
        
        elif forty5 ==' 23':
            asian +=1
            black +=1
        
        elif forty5 =='  2 4':
            asian +=1
            nhpi +=1
        
        elif forty5 ==' 234':
            asian +=1
            black +=1
            nhpi +=1
        
        elif forty5 ==' 2 45':
            asian +=1
            nhpi +=1
            white +=1
            
        
        elif forty5 ==' 2  5':
            asian +=1
            white +=1 
        
        elif forty5 ==' 23 5':
            asian +=1
            black +=1
            white +=1 
        
        elif forty5 ==' 2345':
            asian +=1
            black +=1
            nhpi +=1
            white +=1
        
        elif forty5 =='  3':
            black +=1
        
        elif forty5 =='  345':
            black +=1
            white +=1
            nhpi +=1
            
        elif forty5 =='  3 5':
            white +=1 
            black +=1        
        
        elif forty5 =='   4':
            nhpi +=1
        
        elif forty5 =='  45':
            white +=1 
            nhpi +=1
        
        elif forty5 =='   5':
            white +=1 
        
        
left = [1, 2,3,4,5]
height = [aian,asian,black,nhpi,white]
tick_label = ['AIAN','Asian','Black','NHPI','White']
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('')
plt.ylabel('Race of each student')
plt.title('Race Demographic')
plt.show()

# 5. Students Bullied by Age Chart
# --------------------------------

with open("Standards.csv") as f:
    f.readline()
    
    age12 = 0
    age13 = 0 
    age14 = 0
    age15 = 0
    age16 = 0
    age17 = 0
    age18 = 0
    
    for line in f:
        q23 = line.split(',')[5]
        q1 = line.split(',')[0]
        
        if q23 == '1' and q1 == '1': age12 +=1
        elif q23 == '1' and q1 == '2': age13 +=1
        elif q23 == '1' and q1 == '3': age14 +=1
        elif q23 == '1' and q1 == '4': age15 +=1
        elif q23 == '1' and q1 == '5': age16 +=1
        elif q23 == '1' and q1 == '6': age17 +=1
        elif q23 == '1' and q1 == '7': age18 +=1
        
left = [1, 2, 3, 4, 5, 6, 7]
height = [age12,age13,age14,age15,age16,age17,age18]
tick_label = ['12','13','14','15','16','17','18']
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('Age of Students')
plt.ylabel('Students Bullied')
plt.title('Students Bullied by Age')
plt.show()

# 6. Marijuana Use by Age Chart
# -----------------------------

with open("Standards.csv") as f:
    f.readline()
    
    age12 = 0
    age13 = 0 
    age14 = 0
    age15 = 0
    age16 = 0
    age17 = 0
    age18 = 0
    
    for line in f:
        q45 = line.split(',')[6]
        q1 = line.split(',')[0]
        
        if q1 == '1' and q45 == '1': age12 +=0
        elif q1 == '2' and q45 == '1': age13 +=0
        elif q1 == '3' and q45 == '1': age14 +=0
        elif q1 == '4' and q45 == '1': age15 +=0
        elif q1 == '5' and q45 == '1': age16 +=0
        elif q1 == '6' and q45 == '1': age17 +=0
        elif q1 == '7' and q45 == '1': age18 +=0
        elif q1 == '1' and q45 == '2': age12 +=1.5
        elif q1 == '2' and q45 == '2': age13 +=1.5
        elif q1 == '3' and q45 == '2': age14 +=1.5
        elif q1 == '4' and q45 == '2': age15 +=1.5
        elif q1 == '5' and q45 == '2': age16 +=1.5
        elif q1 == '6' and q45 == '2': age17 +=1.5
        elif q1 == '7' and q45 == '2': age18 +=1.5
        elif q1 == '1' and q45 == '3': age12 +=6
        elif q1 == '2' and q45 == '3': age13 +=6
        elif q1 == '3' and q45 == '3': age14 +=6
        elif q1 == '4' and q45 == '3': age15 +=6
        elif q1 == '5' and q45 == '3': age16 +=6
        elif q1 == '6' and q45 == '3': age17 +=6
        elif q1 == '7' and q45 == '3': age18 +=6
        elif q1 == '1' and q45 == '4': age12 +=15
        elif q1 == '2' and q45 == '4': age13 +=15
        elif q1 == '3' and q45 == '4': age14 +=15
        elif q1 == '4' and q45 == '4': age15 +=15
        elif q1 == '5' and q45 == '4': age16 +=15
        elif q1 == '6' and q45 == '4': age17 +=15
        elif q1 == '7' and q45 == '4': age18 +=15
        elif q1 == '1' and q45 == '5': age12 +=30
        elif q1 == '2' and q45 == '5': age13 +=30
        elif q1 == '3' and q45 == '5': age14 +=30
        elif q1 == '4' and q45 == '5': age15 +=30
        elif q1 == '5' and q45 == '5': age16 +=30
        elif q1 == '6' and q45 == '5': age17 +=30
        elif q1 == '7' and q45 == '5': age18 +=30
        elif q1 == '1' and q45 == '6': age12 +=70
        elif q1 == '2' and q45 == '6': age13 +=70
        elif q1 == '3' and q45 == '6': age14 +=70
        elif q1 == '4' and q45 == '6': age15 +=70
        elif q1 == '5' and q45 == '6': age16 +=70
        elif q1 == '6' and q45 == '6': age17 +=70
        elif q1 == '7' and q45 == '6': age18 +=70
        elif q1 == '1' and q45 == '7': age12 +=100
        elif q1 == '2' and q45 == '7': age13 +=100
        elif q1 == '3' and q45 == '7': age14 +=100
        elif q1 == '4' and q45 == '7': age15 +=100
        elif q1 == '5' and q45 == '7': age16 +=100
        elif q1 == '6' and q45 == '7': age17 +=100
        elif q1 == '7' and q45 == '7': age18 +=100
        
left = [1, 2, 3, 4, 5, 6, 7]
height = [age12,age13,age14,age15,age16,age17,age18]
tick_label = ['12','13','14','15','16','17','18']
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('Age of Students')
plt.ylabel('Times Marijuana Used')
plt.title('Marijuana Used by Age')
plt.show()

# 7. Marijuana Used
# -----------------

with open("Standards.csv") as f:
    f.readline()
    
    zero = 0
    one = 0 
    three = 0
    ten = 0
    twenty = 0
    forty = 0
    hundredup = 0
    
    for line in f:
        q45 = line.split(',')[6]
        
        if q45 == '1': zero +=1
        elif q45 == '2': one +=1
        elif q45 == '3': three +=1
        elif q45 == '4': ten +=1
        elif q45 == '5': twenty +=1
        elif q45 == '6': forty +=1
        elif q45 == '7': hundredup +=1

left = [1, 2, 3, 4, 5, 6, 7]
height = [zero,one,three,ten,twenty,forty,hundredup]
tick_label = ['0','1-2','3-9','10-19','20-39','40-99','100+']
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('Times Marijuana Used')
plt.ylabel('Respondents')
plt.title('Marijuana Used')
plt.show()

# 8. Students Bullied or Not
# --------------------------

with open("Standards.csv") as f:
    f.readline()
    
    bullied = 0
    notbullied = 0

    for line in f:
        q23 = line.split(',')[5]
        q1 = line.split(',')[0]
        
        if q23 == '1': bullied +=1
        elif q23 == '2': notbullied +=1

left = [1, 2]
height = [bullied,notbullied]
tick_label = ['Bullied','Not Bullied']
plt.bar(left, height, tick_label = tick_label,width = 0.8, color = ['maroon']) 
plt.xlabel('Bullied or Not Bullied')
plt.ylabel('Respondents')
plt.title('Students Bullied or Not Bullied')
plt.show()

#Chi Tests

import pandas as pd
from bioinfokit.analys import stat

data = pd.read_csv('Standards.csv')
data.rename(columns={'5':'age','2':'sex','2.1':'grade','1':'Hispanic','1.1':'Race','2.2':'bullied','1.2':'drug use'}, inplace=True)
ctab= pd.crosstab(data.age, data.bullied)
print(ctab)

res = stat()
res.chisq(df =ctab)
print(res.summary)

print(res.expected_df)

data2 = pd.read_csv('Standards.csv')
data2.rename(columns={'5':'age','2':'sex','2.1':'grade','1':'Hispanic','1.1':'Race','2.2':'bullied','1.2':'drug_use'}, inplace=True)
ctab= pd.crosstab(data2.bullied, data2.drug_use)
print(ctab)

res = stat()
res.chisq(df =ctab)
print(res.summary)

print(res.expected_df)
