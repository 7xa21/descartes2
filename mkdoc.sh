#!/bin/sh
javadoc -d doc -sourcepath src -nosince -notimestamp -nodeprecatedlist -noindex -nohelp src/*.java
