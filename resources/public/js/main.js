// let's do this in JS for now
// until we get to ClojureScript and a React Wrapper...

$(".check").on('click', function (e) {
    $.ajax('/update-todo', {
        method: "PUT",
        data: {
            id: $(this).data().id,
            checked: e.target.checked
        }
    }).then(function (id) {
        $("#" + id).toggleClass('strike');
    });
});

$(".delete").on('click', function (e) {
    $.ajax('/delete-todo', {
        method: "DELETE",
        data: {
            id: $(this).data().id
        }
    }).then(function (id) {
        $("#" + id).remove();
    });
});