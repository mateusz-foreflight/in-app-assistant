import MenuChoice from "./MenuChoice";

type MenuChoiceWithChildren = MenuChoice & {
    children: MenuChoice[]
}

export default MenuChoiceWithChildren;