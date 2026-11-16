(function () {
  "use strict";

  function trim(value) {
    return value == null ? "" : String(value).replace(/^\s+|\s+$/g, "");
  }

  function closest(node, attribute) {
    while (node && node.nodeType === 1) {
      if (node.hasAttribute && node.hasAttribute(attribute)) {
        return node;
      }
      node = node.parentNode;
    }
    return null;
  }

  function lookup(name) {
    if (!name) {
      return null;
    }
    var parts = String(name).split("."),
      fn = window,
      i;
    for (i = 0; i < parts.length; i++) {
      if (fn == null) {
        return null;
      }
      fn = fn[parts[i]];
    }
    return typeof fn === "function" ? fn : null;
  }

  function args(element, event, raw) {
    if (!raw) {
      return [];
    }
    var list;
    try {
      list = JSON.parse(raw);
    } catch (e) {
      list = [raw];
    }
    if (!Array.isArray(list)) {
      list = [list];
    }
    return list.map(function (a) {
      if (a === "@this") {
        return element;
      }
      if (a === "@event") {
        return event;
      }
      return a;
    });
  }

  function callNamed(element, event, prefix) {
    var fn = lookup(element.getAttribute(prefix + "-fn")),
      single;
    if (!fn) {
      return;
    }
    single = element.getAttribute(prefix + "-arg");
    if (single !== null) {
      fn.call(element, trim(single));
      return;
    }
    fn.apply(
      element,
      args(element, event, element.getAttribute(prefix + "-args")),
    );
  }

  function formOf(element) {
    if (element.form) {
      return element.form;
    }
    var node = element;
    while (node && node.nodeName !== "FORM") {
      node = node.parentNode;
    }
    return node;
  }

  function applyFieldWrite(element) {
    var name = element.getAttribute("data-csp-set-field"),
      form,
      field,
      candidate,
      i;
    if (!name) {
      return;
    }
    form = formOf(element);
    if (!form) {
      return;
    }
    field = form.elements[name];
    if (!field) {
      for (i = 0; i < form.elements.length; i++) {
        candidate = form.elements[i];
        if (candidate.name && trim(candidate.name) === name) {
          field = candidate;
          break;
        }
      }
    }
    if (field) {
      field.value = element.getAttribute("data-csp-set-value");
    }
  }

  document.addEventListener(
    "click",
    function (e) {
      var el = closest(e.target, "data-csp-nav"),
        href;
      if (el) {
        href = trim(el.getAttribute("data-csp-nav"));
        if (href) {
          window.location.href = href;
          return;
        }
      }

      el = closest(e.target, "data-csp-back");
      if (el) {
        window.history.back();
        return;
      }

      el = closest(e.target, "data-csp-pressed");
      if (el) {
        document.pressed = el.id;
      }

      el = closest(e.target, "data-csp-set-field");
      if (el && el.getAttribute("data-csp-on-click-set") !== null) {
        applyFieldWrite(el);
      }

      el = closest(e.target, "data-csp-click-fn");
      if (el) {
        callNamed(el, e, "data-csp-click");
      }
    },
    false,
  );

  document.addEventListener(
    "change",
    function (e) {
      var el = closest(e.target, "data-csp-rename"),
        form;
      if (el) {
        el.name = el.getAttribute("data-csp-rename");
      }

      el = closest(e.target, "data-csp-change-fn");
      if (el) {
        callNamed(el, e, "data-csp-change");
      }

      el = closest(e.target, "data-csp-submit-change");
      if (el) {
        applyFieldWrite(el);
        form = formOf(el);
        if (form) {
          form.submit();
        }
      }
    },
    false,
  );

  document.addEventListener(
    "submit",
    function (e) {
      var el = closest(e.target, "data-csp-validate"),
        validate;
      if (el) {
        validate = lookup(el.getAttribute("data-csp-validate"));
        if (validate && validate.call(el) === false) {
          e.preventDefault();
          return;
        }
      }

      el = closest(e.target, "data-csp-submit-fn");
      if (el) {
        callNamed(el, e, "data-csp-submit");
      }
    },
    false,
  );

  document.addEventListener(
    "keypress",
    function (e) {
      var el = closest(e.target, "data-csp-keypress-fn");
      if (el) {
        callNamed(el, e, "data-csp-keypress");
      }
    },
    false,
  );

  function blocker(kind) {
    return function (e) {
      var el = closest(e.target, "data-csp-block");
      if (
        el &&
        (" " + el.getAttribute("data-csp-block") + " ").indexOf(
          " " + kind + " ",
        ) >= 0
      ) {
        e.preventDefault();
      }
    };
  }
  document.addEventListener("copy", blocker("copy"), false);
  document.addEventListener("cut", blocker("cut"), false);
  document.addEventListener("paste", blocker("paste"), false);

  function hover(attribute, rowFunction, prefix) {
    return function (e) {
      var el = closest(e.target, "data-csp-hover-row"),
        fn;
      if (el) {
        fn = lookup(rowFunction);
        if (fn) {
          fn(el);
        }
      }
      el = closest(e.target, attribute);
      if (el) {
        callNamed(el, e, prefix);
      }
    };
  }
  document.addEventListener(
    "mouseover",
    hover("data-csp-mouseover-fn", "highlightrow", "data-csp-mouseover"),
    false,
  );
  document.addEventListener(
    "mouseout",
    hover("data-csp-mouseout-fn", "unhighlightrow", "data-csp-mouseout"),
    false,
  );
})();
