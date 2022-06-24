import React from "react";
import {Heading} from "@foreflight/ffui";
import MenuChoiceWithChildren from "../../types/MenuChoiceWithChildren";
import SelectableList from "../selectableList/SelectableList";

type MenuChoiceListProps = {
    choices: MenuChoiceWithChildren[];
    selectCallback: (choice: MenuChoiceWithChildren) => void;
    searchCallback: (searchVal: string) => void;
    selectedChoiceId: number | null;
}

class MenuChoiceList extends React.Component<MenuChoiceListProps, {}>{

    render() {
        return(
            <>
                <Heading>
                    Menu Choice List:
                </Heading>

                <SelectableList<MenuChoiceWithChildren>
                    columnNames={["Menu Choice Name", "Parent Name", "Children", "Resources"]}
                    items={this.props.choices}
                    getIdFunc={choice => choice.id}
                    selectedItemId={this.props.selectedChoiceId}
                    selectCallback={this.props.selectCallback}
                    searchCallback={this.props.searchCallback}
                    columnFuncs={[
                        choice => choice.name,
                        choice => choice.parent ? choice.parent.name : "",
                        choice => {
                            return {raw:
                                    <>{choice.children.map((child) => (
                                        <div key={child.id}>{child.name}</div>
                                    ))}</>
                            };
                        },
                        choice => {
                            return {raw:
                                    <>{choice.resources.map((resource) => (
                                        <div key={resource.id}>{resource.name}</div>
                                    ))}</>
                            };
                        }
                    ]}
                />
            </>
        )
    }
}

export default MenuChoiceList;