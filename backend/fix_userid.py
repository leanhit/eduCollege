import os
import re

files_to_fix = [
    'src/main/java/com/educollege/user/service/TeacherService.java',
    'src/main/java/com/educollege/user/repository/StudentRepository.java',
    'src/main/java/com/educollege/user/repository/TeacherRepository.java',
    'src/main/java/com/educollege/user/controller/TeacherController.java'
]

for filepath in files_to_fix:
    if not os.path.exists(filepath):
        continue
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # replace getUserId() with getUser().getId()
    content = re.sub(r'\.getUserId\(\)', '.getUser().getId()', content)
    
    # replace findByUserId with findByUser_Id
    content = re.sub(r'findByUserId', 'findByUser_Id', content)
    
    # replace existsByUserId with existsByUser_Id
    content = re.sub(r'existsByUserId', 'existsByUser_Id', content)

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

print("UserId fixed.")
