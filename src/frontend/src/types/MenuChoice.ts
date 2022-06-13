import Resource from "./Resource";

type MenuChoice = {
    id: number,
    name: string,
    parent: MenuChoice,
    resources: Resource[]
}

export default MenuChoice;