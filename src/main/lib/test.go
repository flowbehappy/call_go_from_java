package main

import (
	"C"
)
import (
	"strconv"
	"strings"
)

//export Parse
func Parse(input string) *C.char {
	input = input + "waaa"
	input = strings.TrimSuffix(input, "waaa")

	return Wrap2J(input)
}

func Wrap2J(str string) *C.char {
	len := len(str)
	return C.CString(strconv.Itoa(len) + "|" + str)
}

func main() {}
