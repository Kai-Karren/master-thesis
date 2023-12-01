import { SystemResponse, UserResponse } from "@voiceflow/react-chat";

export default function Conversation() {

    const AVATAR = 'https://picsum.photos/seed/1/80/80';

    return (
        <div>
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <SystemResponse messages={[{type: "text", text: "Hello"}]} timestamp={Date.now()} avatar={AVATAR}/>
            <SystemResponse messages={[{type: "text", text: "Hello"}]} timestamp={Date.now()} avatar={AVATAR}/>
            <SystemResponse messages={[{type: "text", text: "Hello"}]} timestamp={Date.now()} avatar={AVATAR}/>
            <SystemResponse messages={[{type: "text", text: "Hello"}]} timestamp={Date.now()} avatar={AVATAR}/>
            <UserResponse message="Hello" timestamp={Date.now()} />
            <UserResponse message="Hello" timestamp={Date.now()} />
            <SystemResponse messages={[{type: "text", text: "Hello"}]} timestamp={Date.now()} avatar={AVATAR}/>
            <SystemResponse messages={[{type: "text", text: "Hello"}]} timestamp={Date.now()} avatar={AVATAR}/>
            <SystemResponse messages={[{type: "text", text: "Hello"}]} timestamp={Date.now()} avatar={AVATAR}/>
        </div>
    )
}