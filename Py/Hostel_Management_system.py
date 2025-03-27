class HostelManagementSystem:
    def __init__(self, male_filename="boys_hostel_data.txt", female_filename="girls_hostel_data.txt"):
        self.male_filename = male_filename
        self.female_filename = female_filename
        result = {}
        for i in range(1, 11):
            result[str(i)] = []
        self.male_rooms = result
        result1 = {}
        for j in range(1, 11):
            result1[str(j)] = []
        self.female_rooms = result1
        self.male_students = []
        self.female_students = []
        self.load_data()
        self.hostel_structure()
        self.menu()

    def menu(self):
        print()
        print("1. Add student")
        print("2. Remove student")
        print("3. View all students")
        print("4. Mess menu")
        print("5. View a particular student I-Card")
        print("6. Search students by room number")
        print("7. Update student information")
        print("8. View seat availability")
        print("9. View all rooms and their occupants")
        print("10. File a complaint")
        print("11. Request room change")
        print("12. Manage events")
        print("13. Exit")
        print()
        choice = int(input("Enter your choice: "))
        print()
        if choice == 1:
            self.add_student()
        elif choice == 2:
            self.remove_student()
        elif choice == 3:
            self.print_students()
        elif choice == 4:
            self.mess_menu()
        elif choice == 5:
            self.view_student_id()
        elif choice == 6:
            self.search_by_room()
        elif choice == 7:
            self.update_student_info()  
        elif choice == 8:
            self.view_seat_availability()
        elif choice == 9:
            self.view_all_rooms()
        elif choice == 10:
            self.file_complaint()
        elif choice == 11:
            self.request_room_change()
        elif choice == 12:
            self.manage_events()
        elif choice == 13:
            print("Exiting the system. Goodbye!")
        else:
            print("Invalid choice. Please try again.")
            self.menu()

    def hostel_structure(self):
        print()
        print("===================================================================")
        print("                  Hostel Management System")
        print("===================================================================")
        print("             Welcome to the SP Hostel - Ahmedabad")
        print("===================================================================")
        print()
        print("-- This hostel is provide 10 rooms for boys and 10 rooms for girls")
        print("-- Also provide a good food facility")
        print()
        print("Boys hostel address : Gulbai tekra - Ahmedabad")
        print("Girls hostel address : Naranpura - Ahmedabad")
        print()
        print("Boys hostel fees")
        print("-- Hostel Fees: Rs. 6000 per month")
        print("-- Mess Fees: Rs. 4000 per month")
        print("-- Total Fees: Rs. 10000 per month")
        print()
        print("Girls hostel fees")
        print("-- Hostel Fees: Rs. 5000 per month")
        print("-- Mess Fees: Rs. 3000 per month")
        print("-- Total Fees: Rs. 8000 per month")

    def add_student(self):
        gender = input("Enter gender (M/F): ").upper()
        if gender == "M":
            if all(len(self.male_rooms[room]) >= 5 for room in self.male_rooms):
                print("All male rooms are full. Cannot add more students.")
                self.menu()
                return
        elif gender == "F":
            if all(len(self.female_rooms[room]) >= 5 for room in self.female_rooms):
                print("All female rooms are full. Cannot add more students.")
                self.menu()
                return
        else:
            print("Invalid gender. Please enter M or F.")
            self.menu()
            return

        p = int(input("Enter your previous year percentage (out of 100): "))
        if p <= 85:
            print("You are not eligible")
            self.menu()
            return

        name = input("Enter student's name: ")
        age = int(input("Enter student's age: "))
        room_no = input("Enter room number (1-10): ")
        if gender == "M" and len(self.male_rooms[room_no]) >= 5:
            print("Room",room_no,"is full. Please choose another room.")
            self.menu()
            return
        elif gender == "F" and len(self.female_rooms[room_no]) >= 5:
            print("Room",room_no,"is full. Please choose another room.")
            self.menu()
            return

        mobile_no = input("Enter your mobile number: ")
        father_mo = input("Enter your father's mobile number: ")
        village = input("Enter your village: ")
        aadhar_no = input("Enter your Aadhar card number: ")
        email = input("Enter email address : ")

        if age > 18:
            student = [name, str(age), room_no, gender, mobile_no, father_mo, village, aadhar_no, email]
            if gender == "M":
                self.male_students.append(student)
                self.male_rooms[room_no].append(student)
                self.save_data(self.male_filename, self.male_students)
            elif gender == "F":
                self.female_students.append(student)
                self.female_rooms[room_no].append(student)
                self.save_data(self.female_filename, self.female_students)
            print()
            print("Student added successfully!")
        else:
            print("You are not eligible")

        self.menu()

    def save_data(self, filename, students):
        with open(filename, "w") as file:
            for student in students:
                file.write(",".join(student) + "\n")

    def load_data(self):
        try:
            with open(self.male_filename, "r") as file:
                for line in file:
                    student = line.strip().split(",")
                    self.male_students.append(student)
                    self.male_rooms[student[2]].append(student)
        except FileNotFoundError:
            pass

        try:
            with open(self.female_filename, "r") as file:
                for line in file:
                    student = line.strip().split(",")
                    self.female_students.append(student)
                    self.female_rooms[student[2]].append(student)
        except FileNotFoundError:
            pass

    def remove_student(self):
        name = input("Enter the name of the student to remove: ")
        student_found = False
        for student in self.male_students:
            if student[0].lower() == name.lower():
                self.male_students.remove(student)
                self.male_rooms[student[2]].remove(student)
                self.save_data(self.male_filename, self.male_students)
                print()
                print("Student", name, "removed successfully!")
                student_found = True
                break
        if not student_found:
            for student in self.female_students:
                if student[0].lower() == name.lower():
                    self.female_students.remove(student)
                    self.female_rooms[student[2]].remove(student)
                    self.save_data(self.female_filename, self.female_students)
                    print()
                    print("Student", name, "removed successfully!")
                    student_found = True
                    break
        if not student_found:
            print(name, "not found")

        self.menu()

    def print_students(self):
        if not self.male_students and not self.female_students:
            print("No student in hostel.")
        else:
            print()
            print("Male Student List :")
            print("Name\tAge   Room No.  Gender    Mobile    Father's Mobile   Village\tAadhar No.\temail")
            for student in self.male_students:
                print("\t".join(student))
            print()
            print("Female Student List :")
            print("Name\tAge   Room No.  Gender    Mobile    Father's Mobile   Village\tAadhar No.\temail")
            for student in self.female_students:
                print("\t".join(student))

        self.menu()

    def view_student_id(self):
        name = input("Enter the name of the student to view: ")
        student_found = False
        for student in self.male_students:
            if student[0].lower() == name.lower():
                self.print_student_id_card(student)
                student_found = True
                break
        if not student_found:
            for student in self.female_students:
                if student[0].lower() == name.lower():
                    self.print_student_id_card(student)
                    student_found = True
                    break
        if not student_found:
            print(name, "not found")

        self.menu()

    def print_student_id_card(self, student):
        print()
        print("Student I-Card")
        print("==============")
        print("Name :",student[0])
        print("Age :",student[1])
        print("Room No. :",student[2])
        print("Gender :",student[3])
        print("Mobile :",student[4])
        print("Father's Mobile :",student[5])
        print("Village :",student[6])
        print("Aadhar No. :",student[7])
        print("Email :",student[8])

    def mess_menu(self):
        print("1. Sunday")
        print("2. Monday")
        print("3. Tuesday")
        print("4. Wednesday")
        print("5. Thursday")
        print("6. Friday")
        print("7. Saturday")
        print()
        ch = int(input("Enter your choice: "))
        print()
        if ch == 1:
            print("Breakfast: fruits")
            print("Lunch: sweet, new dish, chhash")
            print("Dinner: roti, sabji, khichadi, chhash")
        elif ch == 2:
            print("cha bhakhari")
            print("Lunch: roti, sabji, daal, bhaat, salad, chhash")
            print("Dinner: roti, sabji, khichadi, chhash")
        elif ch == 3:
            print("pava bateka")
            print("Lunch: roti, sabji, daal, bhaat, salad, chhash")
            print("Dinner: roti, sabji, jira rice, chhash")
        elif ch == 4:
            print("gathiya")
            print("Lunch: roti, sabji, daal, bhaat, salad, chhash")
            print("Dinner: new dish, chhash")
        elif ch == 5:
            print("sev khamani")
            print("Lunch: roti, sabji, daal, bhaat, salad, chhash")
            print("Dinner: roti, sabji, vagharel bhaat, chhash")
        elif ch == 6:
            print("idali")
            print("Lunch: roti, sabji, daal, bhaat, salad, chhash")
            print("Dinner: new dish, chhash")
        elif ch == 7:
            print("dhokala")
            print("Lunch: roti, sabji, daal, bhaat, salad, chhash")
            print("Dinner: roti, sabji, vagharel khichadi, chhash")
        else:
            print("Please enter valid input")

        self.menu()

    def search_by_room(self):
        room_no = input("Enter room number (1-10): ")
        if room_no in self.male_rooms:
            print("Male Students in Room",room_no,":")
            for student in self.male_rooms[room_no]:
                print("\t".join(student))
        if room_no in self.female_rooms:
            print("Female Students in Room",room_no,":")
            for student in self.female_rooms[room_no]:
                print("\t".join(student))
        self.menu()

    def update_student_info(self):
        name = input("Enter the name of the student to update: ")
        student_found = False
        for student in self.male_students:
            if student[0].lower() == name.lower():
                self.update_student_details(student)
                self.save_data(self.male_filename, self.male_students)
                student_found = True
                break
        if not student_found:
            for student in self.female_students:
                if student[0].lower() == name.lower():
                    self.update_student_details(student)
                    self.save_data(self.female_filename, self.female_students)
                    student_found = True
                    break
        if not student_found:
            print(f"{name} not found")

        self.menu()

    def update_student_details(self, student):
        print("Update Student Details:")
        student[0] = input(f"Name [{student[0]}]: ") or student[0]
        student[1] = input(f"Age [{student[1]}]: ") or student[1]
        student[4] = input(f"Mobile [{student[4]}]: ") or student[4]
        student[5] = input(f"Father's Mobile [{student[5]}]: ") or student[5]
        student[6] = input(f"Village [{student[6]}]: ") or student[6]
        student[7] = input(f"Aadhar No. [{student[7]}]: ") or student[7]
        student[8] = input(f"Email [{student[8]}]: ") or student[8]
        print("Student details updated successfully!")

    def view_seat_availability(self):
        male_available = sum(5 - len(self.male_rooms[room]) for room in self.male_rooms)
        female_available = sum(5 - len(self.female_rooms[room]) for room in self.female_rooms)
        print("Available Male Seat:",male_available)
        print("Available Female Seat:",female_available)
        self.menu()

    def view_all_rooms(self):
        print("Male Rooms and Occupants:")
        for room, students in self.male_rooms.items():
            print("Room ",room,', '.join([student[0] for student in students]))

        print("Female Rooms and Occupants:")
        for room, students in self.female_rooms.items():
            print("Room ",room,', '.join([student[0] for student in students]))

        self.menu()

    def file_complaint(self):
        print("File a Complaint")
        print("================")
        name = input("Enter your name: ")
        issue = input("Enter your complaint: ")
        with open("complaints.txt", "a") as file:
            file.write(f"{name}: {issue}\n")
        print("Complaint filed successfully!")

        self.menu()

    def request_room_change(self):
        print("Request Room Change")
        print("===================")
        name = input("Enter your name: ")
        current_room = input("Enter your current room number: ")
        new_room = input("Enter the new room number you want: ")
        request = f"{name} requests to change from room {current_room} to room {new_room}"
        with open("room_change_requests.txt", "a") as file:
            file.write(request + "\n")
        print("Room change request submitted successfully!")

        self.menu()

    def manage_events(self):
        print("Event Management")
        print("================")
        print("1. View events")
        print("2. Add event")
        choice = int(input("Enter your choice: "))
        if choice == 1:
            self.view_events()
        elif choice == 2:
            self.add_event()
        else:
            print("Invalid choice!")

        self.menu()

    def view_events(self):
        print("Upcoming Events")
        print("===============")
        try:
            with open("events.txt", "r") as file:
                events = file.readlines()
                if not events:
                    print("No upcoming events.")
                for event in events:
                    print(event.strip())
        except FileNotFoundError:
            print("No upcoming events.")

    def add_event(self):
        print("Add Event")
        print("=========")
        event = input("Enter the event details: ")
        with open("events.txt", "a") as file:
            file.write(event + "\n")
        print("Event added successfully!")


if __name__ == "__main__":
    HostelManagementSystem()