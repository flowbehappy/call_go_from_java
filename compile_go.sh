#!/usr/bin/env bash


if [[ "${OSTYPE}" == "linux-gnu" ]] || [[ "${OSTYPE}" == "cygwin" ]]; then
	LIB_SUBFIX=so
elif [[ "${OSTYPE}" == "darwin"* ]]; then
	LIB_SUBFIX=dylib
else
    echo "Platform ${OSTYPE} not supported!"
    exit 1
fi

go build -v -x -buildmode=c-shared -o src/main/lib/libtest.${LIB_SUBFIX} src/main/lib/test.go
mv src/main/lib/libtest.${LIB_SUBFIX} lib/

mvn jnaerator:generate