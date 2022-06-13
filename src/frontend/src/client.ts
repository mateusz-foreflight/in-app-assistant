

const deleteOptions = {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    mode: 'no-cors',
    body: "",
    redirect: 'follow'
};

const postOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    mode: 'no-cors',
    body: '',
    redirect: 'follow'
};

const putOptions = {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    mode: 'no-cors',
    body: '',
    redirect: 'follow'
};

const checkStatus = (response: Response) => {
    if (response.ok) {
        return response;
    }
    // convert non-2xx HTTP responses into errors:
    const error = new Error(response.statusText);
    return Promise.reject(error)
};

export const getTopLevelMenuChoices = () =>
    fetch("http://localhost:8080/api/v1/menuchoices/toplevel").then(checkStatus);

export const getChildrenById = (childId: number) =>
    fetch(`http://localhost:8080/api/v1/menuchoices/${childId}/children`).then(checkStatus);
