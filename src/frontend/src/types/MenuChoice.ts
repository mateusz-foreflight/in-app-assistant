import Resource from "./Resource";

type MenuChoice = {
    id: number,
    name: string,
    parentId: number | null,
    resourceIds: number[],
    isPublic: boolean
}

export default MenuChoice;