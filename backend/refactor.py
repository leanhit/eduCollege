import os
import shutil
import re

source_dir = 'src/main/java/com/chatbot/core/academic'
target_base = 'src/main/java/com/educollege'

def get_module(filename, rel_path):
    folder = os.path.dirname(rel_path)
    
    # Core module
    if folder in ['config', 'exception', 'filter', 'enums', 'common']:
        return 'core'
    # Security/Auth module
    if folder == 'security':
        return 'auth'
        
    name = os.path.basename(filename)
    
    # User module
    if any(x in name for x in ['User', 'Student', 'Teacher', 'Role', 'Profile']):
        return 'user'
    # Auth module
    if any(x in name for x in ['Auth', 'Login', 'Register', 'Jwt', 'Token', 'Credential']):
        return 'auth'
    
    # Default to academic
    return 'academic'

files_to_move = []

for root, dirs, files in os.walk(source_dir):
    for f in files:
        if f.endswith('.java'):
            old_path = os.path.join(root, f)
            rel_path = os.path.relpath(old_path, source_dir)
            module = get_module(f, rel_path)
            
            folder = os.path.dirname(rel_path).replace(os.sep, '.')
            if folder:
                new_pkg = f"com.educollege.{module}.{folder}"
                new_rel_path = f"{module}/{os.path.dirname(rel_path)}/{f}"
            else:
                new_pkg = f"com.educollege.{module}"
                new_rel_path = f"{module}/{f}"
                
            new_path = os.path.join(target_base, new_rel_path)
            
            old_fqn = "com.chatbot.core.academic"
            if folder:
                old_fqn += "." + folder
            old_fqn += "." + f.replace('.java', '')
            
            new_fqn = new_pkg + "." + f.replace('.java', '')
            
            files_to_move.append((old_path, new_path, old_fqn, new_fqn, new_pkg))

# 1. Create directories and copy files
for old_path, new_path, old_fqn, new_fqn, new_pkg in files_to_move:
    os.makedirs(os.path.dirname(new_path), exist_ok=True)
    shutil.copy2(old_path, new_path)

# 2. Rewrite file contents
for _, new_path, _, _, new_pkg in files_to_move:
    with open(new_path, 'r', encoding='utf-8') as file:
        content = file.read()
        
    # Replace package
    content = re.sub(r'^package\s+com\.chatbot\.core\.academic.*?;', f'package {new_pkg};', content, flags=re.MULTILINE)
    
    # Replace specific imports
    for _, _, old_f, new_f, _ in files_to_move:
        content = content.replace(old_f, new_f)
        
    # Replace remaining com.chatbot references
    content = content.replace("com.chatbot.core.academic", "com.educollege.core")
    
    with open(new_path, 'w', encoding='utf-8') as file:
        file.write(content)

# 3. Clean up old directory
if os.path.exists('src/main/java/com/chatbot'):
    shutil.rmtree('src/main/java/com/chatbot')

print("Refactoring complete.")
