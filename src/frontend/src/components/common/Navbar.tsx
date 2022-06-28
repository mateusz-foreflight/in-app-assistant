import React from "react";
import {Button, Row} from "@foreflight/ffui";

type NavbarProps = {
    activePage: number;
}

type pageLink = {
    name: string
    link: string
    id: number
}

class Navbar extends React.Component<NavbarProps, {}>{

    pageLinks: pageLink[] = [
        {name: "Menu Preview", link: "/", id: 0},
        {name: "User Preview", link: "/userPreview", id: 4},
        {name: "Menu Editor", link: "/menuModification", id: 1},
        {name: "Resource Editor", link: "/resourceModification", id: 2},
        {name: "Source Editor", link: "/sourceModification", id:3},
        {name: "Metrics Overview", link: "/metrics", id: 5},
    ]

    render() {
        return (
            <Row>
                {this.pageLinks.map(page => (
                    <Button key={page.id} small color={page.id === this.props.activePage ? "green" : "gray"}
                            onClick={() => {
                                window.location.href = page.link;
                            }}
                    >
                        {page.name}
                    </Button>
                ))}
            </Row>
        );
    }
}

export default Navbar;