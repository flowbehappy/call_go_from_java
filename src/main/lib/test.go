package main

import (
	"C"
)
import (
	"strconv"
	"strings"
)

// Note that the `//export` down their is critical, should keep before every exported methods.

//export Parse
func Parse(input string) *C.char {
	input = input + "waaa"
	input = strings.TrimSuffix(input, "waaa")

	return Wrap2J(input)
}

// Golang cannot return a golang string, because it is a pointer pointed to the memory managed by
// golang runtime, and will be reclaimed in any time. So we must transform it into a *C.char, which is
// a pointer to C style string. And we put it length before it for easier parsing.
//
// Note that the *C.char is a memory which will NOT be recycled by golang runtime, so that the caller must
// release it outside. Check here for detail: https://golang.org/cmd/cgo/
//
func Wrap2J(str string) *C.char {
	len := len(str)
	return C.CString(strconv.Itoa(len) + "|" + str)
}

func main() {}
