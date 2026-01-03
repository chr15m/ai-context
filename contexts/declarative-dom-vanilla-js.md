# Vanilla JS Declarative DOM options

There are several patterns for declarative DOM updates in vanilla JavaScript. Here's an overview:

## Template Literals + innerHTML

The simplest approach—render your UI as a string and set it:

```javascript
function render(state) {
  document.getElementById('app').innerHTML = `
    <h1>${state.title}</h1>
    <ul>${state.items.map(item => `<li>${item}</li>`).join('')}</ul>
    <button onclick="addItem()">Add</button>
  `;
}
```

Downsides: destroys DOM state (focus, selections), potential XSS if you're not careful, and re-renders everything.

## Proxy-Based Reactivity

You can use JavaScript Proxies to automatically trigger re-renders when state changes:

```javascript
function reactive(obj, onChange) {
  return new Proxy(obj, {
    set(target, key, value) {
      target[key] = value;
      onChange();
      return true;
    }
  });
}

const state = reactive({ count: 0 }, render);
state.count++; // automatically calls render()
```

## Tagged Template Literals (htm-style)

You can build lightweight virtual DOM or direct DOM creation using tagged templates:

```javascript
function h(strings, ...values) {
  const template = document.createElement('template');
  template.innerHTML = strings.reduce((acc, str, i) => 
    acc + str + (values[i] ?? ''), '');
  return template.content.cloneNode(true);
}

const el = h`<div class="card"><h2>${title}</h2></div>`;
```

## Simple Diffing

For more efficient updates, you can diff and patch only what changed:

```javascript
function updateElement(parent, newNode, oldNode, index = 0) {
  if (!oldNode) {
    parent.appendChild(newNode);
  } else if (!newNode) {
    parent.removeChild(parent.childNodes[index]);
  } else if (changed(newNode, oldNode)) {
    parent.replaceChild(newNode, parent.childNodes[index]);
  } else if (newNode.nodeType === 1) {
    // Recurse on children
    const newLength = newNode.childNodes.length;
    const oldLength = oldNode.childNodes.length;
    for (let i = 0; i < newLength || i < oldLength; i++) {
      updateElement(oldNode, newNode.childNodes[i], oldNode.childNodes[i], i);
    }
  }
}
```

## Notable Micro-Patterns/Libraries to Study

If you want to see how others have done this minimally, these are worth examining for patterns you could adapt:

- **hyperapp** (~1KB) — tiny functional architecture
- **lit-html** — efficient template rendering via tagged templates, no virtual DOM
- **Sinuous** — reactive primitives without a virtual DOM
- **vanilla-jsx** — shows how to make JSX compile to DOM calls without React

## A Practical Minimal Pattern

Here's a ~30-line pattern that combines these ideas:

```javascript
const createApp = (rootEl, initialState, view) => {
  let state = initialState;
  
  const setState = (update) => {
    state = { ...state, ...update };
    render();
  };
  
  const render = () => {
    const newTree = view(state, setState);
    rootEl.replaceChildren(newTree);
  };
  
  render();
  return { getState: () => state, setState };
};

// Usage
createApp(
  document.getElementById('app'),
  { count: 0 },
  (state, setState) => {
    const div = document.createElement('div');
    div.innerHTML = `<p>Count: ${state.count}</p>`;
    const btn = document.createElement('button');
    btn.textContent = 'Increment';
    btn.onclick = () => setState({ count: state.count + 1 });
    div.appendChild(btn);
    return div;
  }
);
```

The trade-off is always between simplicity and efficiency—full diffing is complex, but `replaceChildren` or `innerHTML` is simple and often fast enough for small apps.
