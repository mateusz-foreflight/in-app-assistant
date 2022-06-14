import MenuChoice from "./types/MenuChoice";
import MenuChoiceDTO from "./types/MenuChoiceDTO";


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
    fetch("http://localhost:8080/api/v1/menuchoices").then(checkStatus);

export const getTopLevelMenuChoices = () =>
    fetch("http://localhost:8080/api/v1/menuchoices/toplevel").then(checkStatus);

export const getChildrenById = (childId: number) =>
    fetch(`http://localhost:8080/api/v1/menuchoices/${childId}/children`).then(checkStatus);

// Update equipment request
export const updateMenuChoice = (choiceId: number, newChoice: MenuChoiceDTO) =>
    fetch(`http://localhost:8080/api/v1/menuchoices/${choiceId}`, putOptions(newChoice)).then(checkStatus);
