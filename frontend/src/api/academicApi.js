import axios from '@/plugins/axios';

export const academicApi = {
    // Faculties
    getFaculties() {
        return axios.get('/api/v1/academic/faculties');
    },
    getFaculty(id) {
        return axios.get(`/api/v1/academic/faculties/${id}`);
    },

    // Departments
    getDepartments() {
        return axios.get('/api/v1/academic/departments');
    },
    getDepartmentsByFaculty(facultyId) {
        return axios.get(`/api/v1/academic/departments/faculty/${facultyId}`);
    },

    // Courses
    getCourses() {
        return axios.get('/api/v1/academic/courses');
    },
    getCourse(id) {
        return axios.get(`/api/v1/academic/courses/${id}`);
    },

    // Course Offerings
    getOfferings(params) {
        return axios.get('/api/v1/academic/offerings', { params });
    },

    // Enrollments
    getStudentEnrollments(studentId) {
        return axios.get(`/api/v1/academic/enrollments/student/${studentId}`);
    },
    enroll(data) {
        return axios.post('/api/v1/academic/enrollments', data);
    },

    // Grades
    getStudentGrades(studentId) {
        return axios.get(`/api/v1/academic/grades/student/${studentId}`);
    }
};

export default academicApi;
