package com.company;


import builder.CourseBuilder;
import builder.StudentBuilder;
import depencyinjection.EnrollmentStorage;
import factory.ProgramHelper;
import factory.ProgramHelperFactory;
import lambda.ProgramMessage;
import sample.Courses;
import sample.Students;
import singleton.EnrollmentStorageSingleton;
import state.ProgramState;
import template.EnrollmentProgram;
import visitor.ProgramMessageSystem;
import visitor.ProgramStartMessage;
import visitor.Visitor;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        programEnrollment();
    }

    private static void programEnrollment() {
        String repeat;

        // SAMPLE
        Students students = new Students();
        students.add(new StudentBuilder().id("S3520920").name("Nguyen Quang Chien").age(21).build());
        students.add(new StudentBuilder().id("S3618658").name("Le Doan Duy").age(22).build());

        Courses courses = new Courses();
        courses.add(new CourseBuilder().id("COSC2440").name("Software Architecture Design and Implementation").numberOfCredits(12).build());
        courses.add(new CourseBuilder().id("COSC2659").name("IOS Development").numberOfCredits(12).build());

        // Program Helper
        ProgramHelper userHelper = ProgramHelperFactory.getHepler("UserHelper");
        userHelper.useStudentSample(students);
        userHelper.useCourseSample(courses);

        // Enrollment program
        EnrollmentStorage enrollmentStorageSingleton = EnrollmentStorageSingleton.getInstance();
        EnrollmentProgram enrollmentProgram = new EnrollmentProgram(enrollmentStorageSingleton, userHelper);

        // Program message
        ProgramMessage repeatMessage = (message) -> System.out.println(message);
        ProgramMessage invalidMessage = (message) -> System.out.println("Invalid: " + message);

        // Program state
        ProgramState programState = new ProgramState();
        programState.next();

        // Main program
        do {
            try {
                ProgramMessageSystem programStartMessage = new ProgramStartMessage();
                programStartMessage.invite(new Visitor());

                Scanner scan = new Scanner(System.in);
                enrollmentProgram.start();
                repeatMessage.print("Would you like to continue? Enter Y to continue or any other key to quit: ");
                repeat = scan.nextLine();
            } catch (InputMismatchException e) {
                invalidMessage.print("input, let's try again");
                repeat = "Y";
            } catch (IndexOutOfBoundsException e) {
                invalidMessage.print("the selection are not avaliable");
                repeat = "Y";
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                repeat = "Y";
            }

        } while (repeat.equalsIgnoreCase("Y"));

        programState.next();
    }
}
