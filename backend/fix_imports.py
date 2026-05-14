import os

files_and_imports = {
    'src/main/java/com/educollege/user/model/Student.java': [
        'import com.educollege.academic.model.Faculty;',
        'import com.educollege.academic.model.Department;',
        'import com.educollege.academic.model.ClassGroup;'
    ],
    'src/main/java/com/educollege/user/model/Teacher.java': [
        'import com.educollege.academic.model.Department;'
    ],
    'src/main/java/com/educollege/academic/model/AdvisingSession.java': [
        'import com.educollege.user.model.Student;',
        'import com.educollege.user.model.Teacher;'
    ],
    'src/main/java/com/educollege/academic/model/CourseOffering.java': [
        'import com.educollege.user.model.Teacher;'
    ],
    'src/main/java/com/educollege/academic/model/Enrollment.java': [
        'import com.educollege.user.model.Student;'
    ],
    'src/main/java/com/educollege/auth/service/AuthenticationService.java': [
        'import com.educollege.academic.service.VietnameseIdService;',
        'import com.educollege.academic.service.VietnameseAcademicValidationService;'
    ]
}

for filepath, imports in files_and_imports.items():
    if not os.path.exists(filepath):
        print(f"Not found: {filepath}")
        continue
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    import_string = '\n' + '\n'.join(imports) + '\n'
    # insert after the first package declaration
    import re
    content = re.sub(r'^(package\s+.*?;)', r'\1' + import_string, content, count=1, flags=re.MULTILINE)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

print("Imports fixed.")
