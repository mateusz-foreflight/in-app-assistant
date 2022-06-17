import MenuChoiceDTO from "./types/MenuChoiceDTO";
import ResourceDTO from "./types/ResourceDTO";

const baseUrl = "http://localhost:8080/api/v1";

const deleteOptions: RequestInit = {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    mode: 'cors',
    body: "",
    redirect: 'follow'
};


function postOptions<T>(item: T) : RequestInit{
    return {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        mode: "cors",
        body: JSON.stringify(item),
        redirect: 'follow'
    }
}

function putOptions<T>(item: T) : RequestInit{
    return {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        mode: "cors",
        body: JSON.stringify(item),
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

export async function extract<T>(resp: Promise<Response>) {
    let fetchedData: T[] = [];

    await resp.then(response => {
        return response.json() as Promise<any[]>;
    }).then(data => {
        data.forEach((datum) => {
            fetchedData.push(datum as T)
        })
    });

    return fetchedData
}


// MENU CHOICES

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



// RESOURCES

export  const getAllResources = () =>
    fetch(baseUrl + "/resources").then(checkStatus);

export const updateResource = (resourceId: number, newResource: ResourceDTO) =>
    fetch(baseUrl + `/resources/${resourceId}`, putOptions(newResource)).then(checkStatus);

export const addResource = (newResource: ResourceDTO) =>
    fetch(baseUrl + "/resources", postOptions(newResource)).then(checkStatus);

export const deleteResource = (resourceId: number, force: boolean = false) =>
    fetch(baseUrl + `/resources/${resourceId}?force=${force}`, deleteOptions).then(checkStatus);