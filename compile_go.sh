#!/usr/bin/env bash
SCRIPT_DIR=$(cd $(dirname $0);echo $PWD)
cd ${SCRIPT_DIR}

if [[ "${OSTYPE}" == "linux-gnu" ]] || [[ "${OSTYPE}" == "cygwin" ]]; then
	LIB_SUBFIX=so
elif [[ "${OSTYPE}" == "darwin"* ]]; then
	LIB_SUBFIX=dylib
else
    echo "Platform ${OSTYPE} not supported!"
    exit 1
fi

go build -buildmode=c-shared -o src/main/lib/libtest.${LIB_SUBFIX} src/main/lib/test.go
rm -rf lib/*
mkdir lib
mv src/main/lib/libtest.${LIB_SUBFIX} lib/

mvn jnaerator:generate -q
