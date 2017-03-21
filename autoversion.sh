#!/bin/sh
SUBJECT=`git log -1 --pretty=%s`
if [ `echo $SUBJECT | grep -c "Merge branch"` -ge 1 ]
	then
		git checkout master
		git pull
		git config --global user.name "Administrator"
		git config --global user.email "gusvmx@gmail.com"
#          Convierte la URL de HTTP parametrizada en una URL de ssh
		export SSH_PUSH_REPO=`echo $CI_BUILD_REPO | perl -pe 's#.*@(.+?(\:\d+)?)/#git@\1:#'`
		git remote set-url --push origin "$SSH_PUSH_REPO"
		gradle clean release -Prelease.useAutomaticVersion=true
fi

