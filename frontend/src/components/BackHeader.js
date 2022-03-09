import React from 'react';
import {Container, Navbar} from "react-bootstrap";

const BackHeader = () => {
    return (
        <div>
            <Navbar bg="light" expand="lg">
                <Container fluid>
                    <Navbar.Brand href="/simulation">Go back</Navbar.Brand>
                </Container>
            </Navbar>
        </div>
    );
};

export default BackHeader;