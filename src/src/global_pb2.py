# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: global.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


import common_pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='global.proto',
  package='',
  serialized_pb=_b('\n\x0cglobal.proto\x1a\x0c\x63ommon.proto\"H\n\x0cGlobalHeader\x12\x12\n\ncluster_id\x18\x01 \x02(\x05\x12\x0c\n\x04time\x18\x02 \x02(\x03\x12\x16\n\x0e\x64\x65stination_id\x18\x08 \x01(\x05\"\xf4\x01\n\rGlobalMessage\x12#\n\x0cglobalHeader\x18\x01 \x02(\x0b\x32\r.GlobalHeader\x12\x0e\n\x04ping\x18\x02 \x01(\x08H\x00\x12\x11\n\x07message\x18\x03 \x01(\tH\x00\x12\x1b\n\x07request\x18\x04 \x01(\x0b\x32\x08.RequestH\x00\x12\x1d\n\x08response\x18\x05 \x01(\x0b\x32\t.ResponseH\x00\x12*\n\x12whoIsClusterLeader\x18\x06 \x01(\x0b\x32\x0c.WhoIsLeaderH\x00\x12(\n\x11\x63lusterLeaderInfo\x18\x07 \x01(\x0b\x32\x0b.LeaderInfoH\x00\x42\t\n\x07payload\"9\n\x0bWhoIsLeader\x12\x13\n\x0brequesterIp\x18\x01 \x02(\t\x12\x15\n\rrequesterPort\x18\x02 \x02(\x05\"2\n\nLeaderInfo\x12\x10\n\x08leaderIp\x18\x01 \x02(\t\x12\x12\n\nleaderPort\x18\x02 \x02(\x05\x42\n\n\x06globalH\x01')
  ,
  dependencies=[common_pb2.DESCRIPTOR,])
_sym_db.RegisterFileDescriptor(DESCRIPTOR)




_GLOBALHEADER = _descriptor.Descriptor(
  name='GlobalHeader',
  full_name='GlobalHeader',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='cluster_id', full_name='GlobalHeader.cluster_id', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='time', full_name='GlobalHeader.time', index=1,
      number=2, type=3, cpp_type=2, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='destination_id', full_name='GlobalHeader.destination_id', index=2,
      number=8, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=30,
  serialized_end=102,
)


_GLOBALMESSAGE = _descriptor.Descriptor(
  name='GlobalMessage',
  full_name='GlobalMessage',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='globalHeader', full_name='GlobalMessage.globalHeader', index=0,
      number=1, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='ping', full_name='GlobalMessage.ping', index=1,
      number=2, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='message', full_name='GlobalMessage.message', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='request', full_name='GlobalMessage.request', index=3,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='response', full_name='GlobalMessage.response', index=4,
      number=5, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='whoIsClusterLeader', full_name='GlobalMessage.whoIsClusterLeader', index=5,
      number=6, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='clusterLeaderInfo', full_name='GlobalMessage.clusterLeaderInfo', index=6,
      number=7, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  oneofs=[
    _descriptor.OneofDescriptor(
      name='payload', full_name='GlobalMessage.payload',
      index=0, containing_type=None, fields=[]),
  ],
  serialized_start=105,
  serialized_end=349,
)


_WHOISLEADER = _descriptor.Descriptor(
  name='WhoIsLeader',
  full_name='WhoIsLeader',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='requesterIp', full_name='WhoIsLeader.requesterIp', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='requesterPort', full_name='WhoIsLeader.requesterPort', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=351,
  serialized_end=408,
)


_LEADERINFO = _descriptor.Descriptor(
  name='LeaderInfo',
  full_name='LeaderInfo',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='leaderIp', full_name='LeaderInfo.leaderIp', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='leaderPort', full_name='LeaderInfo.leaderPort', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=410,
  serialized_end=460,
)

_GLOBALMESSAGE.fields_by_name['globalHeader'].message_type = _GLOBALHEADER
_GLOBALMESSAGE.fields_by_name['request'].message_type = common_pb2._REQUEST
_GLOBALMESSAGE.fields_by_name['response'].message_type = common_pb2._RESPONSE
_GLOBALMESSAGE.fields_by_name['whoIsClusterLeader'].message_type = _WHOISLEADER
_GLOBALMESSAGE.fields_by_name['clusterLeaderInfo'].message_type = _LEADERINFO
_GLOBALMESSAGE.oneofs_by_name['payload'].fields.append(
  _GLOBALMESSAGE.fields_by_name['ping'])
_GLOBALMESSAGE.fields_by_name['ping'].containing_oneof = _GLOBALMESSAGE.oneofs_by_name['payload']
_GLOBALMESSAGE.oneofs_by_name['payload'].fields.append(
  _GLOBALMESSAGE.fields_by_name['message'])
_GLOBALMESSAGE.fields_by_name['message'].containing_oneof = _GLOBALMESSAGE.oneofs_by_name['payload']
_GLOBALMESSAGE.oneofs_by_name['payload'].fields.append(
  _GLOBALMESSAGE.fields_by_name['request'])
_GLOBALMESSAGE.fields_by_name['request'].containing_oneof = _GLOBALMESSAGE.oneofs_by_name['payload']
_GLOBALMESSAGE.oneofs_by_name['payload'].fields.append(
  _GLOBALMESSAGE.fields_by_name['response'])
_GLOBALMESSAGE.fields_by_name['response'].containing_oneof = _GLOBALMESSAGE.oneofs_by_name['payload']
_GLOBALMESSAGE.oneofs_by_name['payload'].fields.append(
  _GLOBALMESSAGE.fields_by_name['whoIsClusterLeader'])
_GLOBALMESSAGE.fields_by_name['whoIsClusterLeader'].containing_oneof = _GLOBALMESSAGE.oneofs_by_name['payload']
_GLOBALMESSAGE.oneofs_by_name['payload'].fields.append(
  _GLOBALMESSAGE.fields_by_name['clusterLeaderInfo'])
_GLOBALMESSAGE.fields_by_name['clusterLeaderInfo'].containing_oneof = _GLOBALMESSAGE.oneofs_by_name['payload']
DESCRIPTOR.message_types_by_name['GlobalHeader'] = _GLOBALHEADER
DESCRIPTOR.message_types_by_name['GlobalMessage'] = _GLOBALMESSAGE
DESCRIPTOR.message_types_by_name['WhoIsLeader'] = _WHOISLEADER
DESCRIPTOR.message_types_by_name['LeaderInfo'] = _LEADERINFO

GlobalHeader = _reflection.GeneratedProtocolMessageType('GlobalHeader', (_message.Message,), dict(
  DESCRIPTOR = _GLOBALHEADER,
  __module__ = 'global_pb2'
  # @@protoc_insertion_point(class_scope:GlobalHeader)
  ))
_sym_db.RegisterMessage(GlobalHeader)

GlobalMessage = _reflection.GeneratedProtocolMessageType('GlobalMessage', (_message.Message,), dict(
  DESCRIPTOR = _GLOBALMESSAGE,
  __module__ = 'global_pb2'
  # @@protoc_insertion_point(class_scope:GlobalMessage)
  ))
_sym_db.RegisterMessage(GlobalMessage)

WhoIsLeader = _reflection.GeneratedProtocolMessageType('WhoIsLeader', (_message.Message,), dict(
  DESCRIPTOR = _WHOISLEADER,
  __module__ = 'global_pb2'
  # @@protoc_insertion_point(class_scope:WhoIsLeader)
  ))
_sym_db.RegisterMessage(WhoIsLeader)

LeaderInfo = _reflection.GeneratedProtocolMessageType('LeaderInfo', (_message.Message,), dict(
  DESCRIPTOR = _LEADERINFO,
  __module__ = 'global_pb2'
  # @@protoc_insertion_point(class_scope:LeaderInfo)
  ))
_sym_db.RegisterMessage(LeaderInfo)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), _b('\n\006globalH\001'))
# @@protoc_insertion_point(module_scope)
