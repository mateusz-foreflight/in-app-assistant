import React from "react";
import {Heading} from "@foreflight/ffui";
import SelectableList from "../selectableList/SelectableList";
import Source from "../../types/Source";

type SourceListProps = {
    sources: Source[];
    selectCallback: (source: Source) => void;
    selectedSourceId: number | null;
}

class SourceList extends React.Component<SourceListProps, {}>{

    render() {
        return (
            <>
                <Heading>
                    Source List:
                </Heading>

                <SelectableList<Source>
                    columnNames={["Source Name", "Link"]}
                    items={this.props.sources}
                    getIdFunc={source => source.id}
                    selectedItemId={this.props.selectedSourceId}
                    selectCallback={this.props.selectCallback}
                    columnFuncs={[
                        source => <>{source.name}</>,
                        source => <>{source.link}</>,
                    ]}
                />
            </>
        );
    }
}

export default SourceList;