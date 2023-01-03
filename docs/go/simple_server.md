一个简单的demo，创建一个web服务，以json格式返回当前时间

```go
package main

import (
	"encoding/json"
	"io"
	"net/http"
	"strings"
	"time"
)

type data struct {
	Now int64 `json:"now"`
}

func Now() *data {
	now := time.Now().Unix()
	d := data{
		Now: now,
	}
	return &d
}

func JSONHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application-json")
	var ret, _ = json.Marshal(Now())
	io.Copy(w, strings.NewReader(string(ret)))
}

func main() {
	http.HandleFunc("/json", JSONHandler)
	http.ListenAndServe(":18888", nil)
}
```



执行注册的DefaultServeMux对象，从字面上看，这是个加锁的方法

```go
// HandleFunc registers the handler function for the given pattern in the DefaultServeMux.
// The documentation for ServeMux explains how patterns are matched.
func HandleFunc(pattern string, handler func(ResponseWriter, *Request)) {
	DefaultServeMux.HandleFunc(pattern, handler)
}

// HandleFunc registers the handler function for the given pattern.
func (mux *ServeMux) HandleFunc(pattern string, handler func(ResponseWriter, *Request)) {
	if handler == nil {
		panic("http: nil handler")
	}
	mux.Handle(pattern, HandlerFunc(handler))
}
```

执行pattern和handler的绑定

从代码可以看到ServeMux的m属性是绑定的位置，数据类型是map[string]muxEntry

```go
// Handle registers the handler for the given pattern.
// If a handler already exists for pattern, Handle panics.
func (mux *ServeMux) Handle(pattern string, handler Handler) {
	mux.mu.Lock()
	defer mux.mu.Unlock()

	if pattern == "" {
		panic("http: invalid pattern")
	}
	if handler == nil {
		panic("http: nil handler")
	}
	if _, exist := mux.m[pattern]; exist {
		panic("http: multiple registrations for " + pattern)
	}

	if mux.m == nil {
		mux.m = make(map[string]muxEntry)
	}
	e := muxEntry{h: handler, pattern: pattern}
	mux.m[pattern] = e
	if pattern[len(pattern)-1] == '/' {
		mux.es = appendSorted(mux.es, e)
	}

	if pattern[0] != '/' {
		mux.hosts = true
	}
}

type ServeMux struct {
	mu    sync.RWMutex
	m     map[string]muxEntry
	es    []muxEntry // slice of entries sorted from longest to shortest.
	hosts bool       // whether any patterns contain hostnames
}

type muxEntry struct {
	h       Handler
	pattern string
}
```

