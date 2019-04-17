components = {}

ComponentType = {
    "subsystem": 0,
    "command": 1,
    "commandgroup": 2,
    "utility": 3
}

# Used for looking up a name from a number
reverse_ComponentType = dict((str(v),k) for k,v in ComponentType.items())

def register(name: str, type: ComponentType):
    components[name] = {"type": reverse_ComponentType[str(type)]}

def unregister(name: str):
    del components[name]