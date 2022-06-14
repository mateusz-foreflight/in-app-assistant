import React from "react";
import {Button, Row, Table, TBody, Td, TdHeader, TFoot, THead, Tr} from "@foreflight/ffui";
import MenuChoice from "../../types/MenuChoice";
import {getChildrenById} from "../../client";
import MenuChoiceWithChildren from "../../types/MenuChoiceWithChildren";






type MenuChoiceListProps = {
    choices: MenuChoiceWithChildren[];
    selectCallback: (choice: MenuChoiceWithChildren) => void;
    selectedChoiceId: number | null;
}




class MenuChoiceList extends React.Component<MenuChoiceListProps, {}>{

    constructor(props: MenuChoiceListProps) {
        super(props);
    }

    selectFunc(choice: MenuChoiceWithChildren){
        this.props.selectCallback(choice);
    }


    render() {
        return(
            <>
            Menu Choice List:
            <Row width={"35%"}>
                <Table striped={true}>
                    <THead>
                        <Tr>
                            <TdHeader defaultWidth={"5px"}></TdHeader>
                            <TdHeader>Menu Choice Name</TdHeader>
                            <TdHeader>Parent Name</TdHeader>
                            <TdHeader>Children</TdHeader>
                            <TdHeader>Resources</TdHeader>
                        </Tr>
                    </THead>
                    <TBody>
                        {this.props.choices.map((choice, idx) => (
                            <Tr key={choice.id}>
                                <Td>
                                    <Button small color={this.props.selectedChoiceId == choice.id ? "green" : "blue"} onClick={() => {
                                        this.selectFunc(choice);
                                    }}>
                                        Select
                                    </Button>
                                </Td>
                                <Td>
                                    {choice.name}
                                </Td>
                                <Td>
                                    {choice.parent ? choice.parent.name : ""}
                                </Td>
                                <Td>
                                    {choice.children.map((child) => (
                                        <div key={child.id}>{child.name}</div>
                                    ))}
                                </Td>
                                <Td>
                                    {choice.resources.map((resource) => (
                                        <div key={resource.id}>{resource.name}</div>
                                    ))}
                                </Td>
                            </Tr>
                        ))}
                    </TBody>
                </Table>
            </Row>
            </>
        )
    }
}

export default MenuChoiceList;