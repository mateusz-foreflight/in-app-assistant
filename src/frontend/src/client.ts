import MenuChoiceDTO from "./types/MenuChoiceDTO";

const baseUrl = "http://localhost:8080/api/v1";

const deleteOptions: RequestInit = {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    mode: 'cors',
    body: "",
    redirect: 'follow'
};


function postOptions(choice: MenuChoiceDTO) : RequestInit{
    return {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        mode: "cors",
        body: JSON.stringify(choice),
        redirect: 'follow'
    }
}

function putOptions(choice: MenuChoiceDTO) : RequestInit{
    return {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        mode: "cors",
        body: JSON.stringify(choice),
        redirect: 'follow'
    }
}

const checkStatus = async (response: Response) => {
    if (response.ok) {
        return response;
    }
    // convert non-2xx HTTP responses into errors:
    const error = new Error(await response.text());
    return Promise.reject(error)
};

export const getAllMenuChoices = () =>
    fetch(baseUrl + "/menuchoices").then(checkStatus);

export const getTopLevelMenuChoices = () =>
    fetch(baseUrl + "/menuchoices/toplevel").then(checkStatus);

export const getChildrenById = (childId: number) =>
    fetch(baseUrl + `/menuchoices/${childId}/children`).then(checkStatus);

export const updateMenuChoice = (choiceId: number, newChoice: MenuChoiceDTO) =>
    fetch(baseUrl + `/menuchoices/${choiceId}`, putOptions(newChoice)).then(checkStatus);

export const addMenuChoice = (newChoice: MenuChoiceDTO) =>
    fetch(baseUrl + "/menuchoices", postOptions(newChoice)).then(checkStatus);

export const deleteMenuChoice = (choiceId: number) =>
    fetch(baseUrl + `/menuchoices/${choiceId}`, deleteOptions).then(checkStatus);

export  const getAllResources = () =>
    fetch(baseUrl + "/resources").then(checkStatus);