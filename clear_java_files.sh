#!/bin/bash

# Script to clear all Java files while keeping only package declaration and class/interface declaration

find . -name "*.java" -type f | while read -r file; do
    echo "Processing: $file"
    
    # Extract package from the file path
    package_path=$(dirname "$file" | sed 's|^\./src/||' | sed 's|/|.|g')
    
    # Read the first few lines to determine if it's a class, interface, or enum
    if grep -q "public class" "$file"; then
        class_name=$(grep "public class" "$file" | head -1 | sed 's/.*public class \([A-Za-z0-9_]*\).*/\1/')
        echo "package $package_path;" > "$file"
        echo "" >> "$file"
        echo "public class $class_name {" >> "$file"
        echo "    " >> "$file"
        echo "}" >> "$file"
    elif grep -q "public interface" "$file"; then
        interface_name=$(grep "public interface" "$file" | head -1 | sed 's/.*public interface \([A-Za-z0-9_]*\).*/\1/')
        echo "package $package_path;" > "$file"
        echo "" >> "$file"
        echo "public interface $interface_name {" >> "$file"
        echo "    " >> "$file"
        echo "}" >> "$file"
    elif grep -q "public enum" "$file"; then
        enum_name=$(grep "public enum" "$file" | head -1 | sed 's/.*public enum \([A-Za-z0-9_]*\).*/\1/')
        echo "package $package_path;" > "$file"
        echo "" >> "$file"
        echo "public enum $enum_name {" >> "$file"
        echo "    " >> "$file"
        echo "}" >> "$file"
    elif grep -q "class.*{" "$file"; then
        class_name=$(grep "class" "$file" | head -1 | sed 's/.*class \([A-Za-z0-9_]*\).*/\1/')
        echo "package $package_path;" > "$file"
        echo "" >> "$file"
        echo "class $class_name {" >> "$file"
        echo "    " >> "$file"
        echo "}" >> "$file"
    else
        # Fallback - try to extract filename as class name
        filename=$(basename "$file" .java)
        echo "package $package_path;" > "$file"
        echo "" >> "$file"
        echo "public class $filename {" >> "$file"
        echo "    " >> "$file"
        echo "}" >> "$file"
    fi
    
    echo "Cleared: $file"
done

echo "All Java files have been cleared!"
